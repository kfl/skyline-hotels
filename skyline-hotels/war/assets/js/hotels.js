"use strict";

var hotels;
var skyline;
function inSkyline(hotel, sky) {
    sky = sky || skyline;
    return $.grep(sky, function(e){ return e.id == hotel.id; }).length > 0;
}

function removeDuplicates(hotels) {
    var seen = {}; var res = [];
    $.each(hotels, function(i, e) {
        if (!seen[e.id]) {
            seen[e.id] = true;
            res.push(e)
        }
    });      
    return res;
}

function populateList(hs, sky) {
    $("#hotel-info").text("Found "+hs.length+' hotels, '+sky.length+' suggested.');
    var resultOpt = $('input[name=resultOpt]:checked', '#uiOpts').val();
    if( resultOpt == "highlight" ) {
        pager(hs);
    } else {
        console.log("Pushup");
        var filtered = $.grep(hs, function(e){ return !inSkyline(e, sky); });
        pager( $.merge($.merge([], sky), filtered) );
    }
}

function pager(hotels) {
    var offset = 10;
    $("#pager-prev").removeClass("disabled");

    function showing(len) {
        $("#pager-info").text("Showing hotels "+(offset+1)+'-'+(offset+len)+' out of '+hotels.length);
    }

    function next() {
        if (! $("#pager-next").hasClass("disabled") ) {
            console.log('next: '+offset);
            $("#pager-prev").removeClass("disabled");
            offset += 10;
            var hs = hotels.slice(offset, offset + 10);
            if (offset+10 > hotels.length) {
                $("#pager-next").addClass("disabled");
            }
            showing(hs.length);
            makeHotelListing(hs);
        }
    }

    function prev() {
        if (! $("#pager-prev").hasClass("disabled") ) {
            console.log('prev: '+offset);
            offset -= 10;
            var hs = hotels.slice(offset, offset + 10);
            if (offset-10 < 0) {
                $("#pager-prev").addClass("disabled");
            }
            if (offset+10 > hotels.length) {
                $("#pager-next").addClass("disabled");
            } else {
                $("#pager-next").removeClass("disabled");
            }
            showing(hs.length);
            makeHotelListing(hs);
        }
    }

    prev();
    $("#pager-next").click(next);
    $("#pager-prev").click(prev);
}



function makeHotelListing(hs) {
    clearList();
    console.log("Populating list");
    var hlist = $("#hotel-list");

    $(hs).each(function(index, item) {
        var hotelElm = $(document.createElement('li'))
            .attr("class", "media")
            .html(htmlHotel(item));

        hlist.append(hotelElm);
    });

}

var debug_flag = false; 
function debug_output(str) {
    return debug_flag ? str : '';
}

function modalRef(hotel) {
    return 'modal_'+hotel.id;
}

function modalDetails(hotel) {
    var details = hotelDetails(hotel);
    var modalR = modalRef(hotel);
    return '<div id="'+modalR+'" class="modal hide fade" tabindex="-1" '+ 
        'role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
        '<div class="modal-header">'+
        '<button type="button" class="close" '+
        'data-dismiss="modal" aria-hidden="true">×</button>'+
        '<h3 id="myModalLabel">'+details.header+'</h3></div>'+
        '<div class="modal-body">'+
        '<div class="row-fluid"><div class="span4">'+details.img+
        '</div><div class="span8">'+details.body+'</div></div></div>'+
        '<div class="modal-footer">'+
        '<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>'+
        '</div></div>';
}


function htmlHotel(hotel){
    var imgP = /low|high/.test(hotel.picture) ? 
        'assets/data/'+hotel.picture : "assets/data/hotel_images/high/na.jpg";
    var modalR = modalRef(hotel);
    var klass = inSkyline(hotel) ? "media-body list-highlight" : "media-body";

    return '<img class="media-object pull-left img-polaroid" src="'+imgP+'">'+
        '<div class="'+klass+'"><h4 class="media-heading">'+hotel.name+'</h4>'+
        '<p>'+hotel.address1+', '+hotel.postalCode+' '+hotel.city+
        '<a href="#'+modalR+
        '" role="button" class="btn pull-right" data-toggle="modal" >'+
        '<i class="icon-info-sign"></i> Details</a>'+
        '<br />'+
        'Price per Night: '+hotel.price.toFixed(0)+'€<br />'+
        debug_output('(id: '+hotel.id+', order: '+hotel.order+')')+
        '</p></div>'+modalDetails(hotel);
}


function hotelDetails (hotel) {
    var imgP = /low|high/.test(hotel.picture) ? 
        'assets/data/'+hotel.picture : "assets/data/hotel_images/high/na.jpg";
    return {header: hotel.name,
            img: '<img class="details-img img-rounded" src="'+imgP+'">',
            body:
            '<p>'+hotel.shortDescription+'</p><hr/>'+
            'Price per Night: '+hotel.price.toFixed(0)+'€<br />'+
            'Pool: '+ (hotel.pool ? 'Yes' : 'No') + ', '+
            'Internet: '+ (hotel.pool ? 'Yes' : 'No') + '<br />'+
            'Distance from Colosseum: '+(hotel.distFromColosseum/1000).toFixed(2)+'km <br />'+
            'Distance from Trevi Fountain: '+(hotel.distFromTreviFountain/1000).toFixed(2)+'km <br />'+
            'Distance from the Vatican: '+(hotel.distFromVatican/1000).toFixed(2)+'km <br />'+
            'Expedia Rating: '+hotel.hotelRating.toFixed(1)+', '+
            'Trip Advisor Rating: '+hotel.tripAdvisorRating.toFixed(1)+''+
            '</p>'};
}


function clearList() {
    $("#hotel-list").empty();
}

function showAlert(message, kind) {
    $("#hotel-list")
        .append($('<div></div>')
                .html(message)
                .attr("class", "alert alert-block "+kind));
}

function getRange( selector, factor ) {
    factor = factor || 1;
    var values = $(selector).slider("values");
    var options = $(selector).slider("option");
    values[0] = values[0] == options.min ? undefined : values[0] * factor;
    values[1] = values[1] == options.max ? undefined : values[1] * factor;
    return values;
}

function scaleToMeters( vals ) {
    return $.map(vals, function(e) { return e ? e * 1000 : undefined; });
}

function showFailure ( jqxhr, textStatus, error ) {
    clearList();
    var err = jqxhr.status + ' ' + textStatus + ': ' + error+jqxhr.responseText;
    console.log(jqxhr.responseText);
    showAlert('<h4>Oh snap, something went wrong</h4>'+err,
              'alert-error');
}

function getSkylineAtts(query) {
    var inputOpt = $('input[name=inputOpt]:checked', '#uiOpts').val();
    console.log(inputOpt);

    if( inputOpt == "implicit" ) {
        var res = [];
        var atts = $.map($("input[name='skyline-opt']"), function(a){
            var e = a.value;
            console.log(e);
            return (query[e+'Start'] || query[e+'End']) ? e : undefined;
        });
        return atts.filter(function(){return true});
    } else {
        return $.map($("input[name='skyline-opt']:checked"), 
                     function(e,i){ return e.value; });
    }

}


function getHotels() {
    clearList();
    showAlert('<h4>Getting Data</h4>Kick back and put your feet up while we work.',
              'alert-info');
    $("#spinner").css('visibility','visible');


    var colRange   = getRange( "#colosseum-range", 1000 );
    var treviRange = getRange( "#trevi-range", 1000 );
    var proxRange  = getRange( "#proximity-range", 1000 );
    var rateRange  = getRange( "#rate-range" );
    var expedia    = $( "#expedia-range" ).slider("value");
    var tripad     = $( "#tripad-range" ).slider("value");
    var sorting    = $( "#sortby" ).val();
    var query = {priceStart: rateRange[0],
                 priceEnd: rateRange[1],
                 distFromVaticanStart: proxRange[0],
                 distFromVaticanEnd: proxRange[1],
                 distFromColosseumStart: colRange[0],
                 distFromColosseumEnd: colRange[1],
                 distFromTreviFountainStart: treviRange[0],
                 distFromTreviFountainEnd: treviRange[1],
                 pool: $("#pool").is(':checked'),
                 internet: $("#internet").is(':checked'),
                 hotelRatingStart: expedia == 0 ? undefined : expedia,
                 tripAdvisorRatingStart: tripad == 0 ? undefined : tripad
                };

    if (sorting != "default" ) {
        var parts = sorting.split("-");
        query['sortBy'] = parts[0];
        query['sortType'] = parts[1];
    } 

    jQuery.getJSON("/skyline_hotels",
                   query,
                   function(hs) {
                       hotels = hs;
                       var satt = getSkylineAtts(query);
                       console.log(satt);
                       if (satt.length > 0) {
                           query['skylineOf'] = satt.join(',');
                           query['sortBy'] = "skyline";
                           jQuery.getJSON("/skyline_hotels",
                                          query,
                                          function(sky) {
                                              skyline = removeDuplicates(sky);
                                              populateList(hs, skyline);
                                              populateMap(hs, skyline);
                                              $("#spinner").css('visibility','hidden');
                                          })
                               .fail(showFailure);
                       } else {
                           skyline = [];
                           populateList(hs, []);
                           populateMap(hs, []);
                           $("#spinner").css('visibility','hidden');
                       }
                   })
        .fail(showFailure);
}


var gmap;
var markersArray = [];

function addHotelMarker(hotel) {
    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(hotel.lat, hotel.lon),
        map: gmap,
        icon: inSkyline(hotel) ? "https://chart.googleapis.com/chart?chst=d_map_xpin_icon_withshadow&chld=pin_star|home|FFFF42|FF0000" : undefined,
        title: name
    });

    var details = hotelDetails(hotel);
    
    var infowindow = new google.maps.InfoWindow({
        content: '<div class="map-details"><h4>'+details.header+'</h4><p>'+details.img+'</p>'+details.body+'</div>',
        maxWidth: 300 
    });

    google.maps.event.addListener(marker, 'click', function() {
        infowindow.open(gmap,marker);
    });

    markersArray.push(marker);
    return marker;
}

// Removes the overlays from the map, but keeps them in the array
function clearHotelOverlays() {
  if (markersArray) {
    for (var i in markersArray) {
      markersArray[i].setMap(null);
    }
  }
}

// Shows any overlays currently in the array
function showHotelOverlays() {
  if (markersArray) {
    for (var i in markersArray) {
      markersArray[i].setMap(gmap);
    }
  }
}

// Deletes all markers in the array by removing references to them
function deleteHotelOverlays() {
  if (markersArray) {
    for (var i in markersArray) {
      markersArray[i].setMap(null);
    }
    markersArray.length = 0;
  }
}


function populateMap(hs, sky){
    var resultOpt = $('input[name=resultOpt]:checked', '#uiOpts').val();
    if( resultOpt == "highlight" ) {
        makeMapMarkers(hs);
    } else {
        makeMapMarkers(sky);
    }
}

function makeMapMarkers(hs) {
    console.log("Populating map");
    deleteHotelOverlays()
    var sky = []
    $.each(hs, function(index, hotel) {
        var marker = addHotelMarker(hotel);
        if (inSkyline(hotel)) sky.push(marker);
    });
    $.each(sky, function(i, marker) { marker.setZIndex(google.maps.Marker.MAX_ZINDEX + 1); });

    if (sky.length > 0) gmap.panTo(sky[0].getPosition());
    else if(hs.length > 0) gmap.panTo(new google.maps.LatLng(hs[0].lat, hs[0].lon));
}

function setupRangeCheckbox( selector ) {
    $("#"+selector+"-lab").siblings("input[name='skyline-opt']").change(function() {
        if ($(this).is(':checked')) {
            $( "#"+selector+"-range" ).slider( "enable" );
        } else {
            $( "#"+selector+"-range" ).slider( "disable" );
            $("#"+selector+"-lab").text('');
            var options = $( "#"+selector+"-range" ).slider("option");
            if ( typeof(options.range) == "boolean" ) {
                $( "#"+selector+"-range" ).slider( "option", "values", 
                                                   [options.min, options.max] );
            } else {
                $( "#"+selector+"-range" ).slider( "option", "value",
                                                   [options.min] );
            }
        }
        getHotels();
    });
}

function resetFilters() {
    $('input[type="checkbox"]').prop('checked', false);
    $('.sliderwrapper').each(function(i,e){
        var options = $(this).slider("option");
        $(this).off("slidechange");
        if ( typeof(options.range) == "boolean" )
            $(this).slider( "option", "values", [options.min, options.max] );
        else
            $(this).slider( "option", "value", [options.min] );
        if ( $('input[name="inputOpt"]:checked', '#uiOpts').val() == "implicit")
            $(this).slider( "enable" );
        else
            $(this).slider( "disable" );
        $(this).on("slidechange", getHotels);
    });
    $(".range-lab").text('');

    getHotels();
}


$(function() {

    $('#internet,#pool').change(getHotels);
    $("#sortby").change(getHotels);
    $(".brand").click(resetFilters);

    $('input[name="resultOpt"]', '#uiOpts').change(function() {
        clearList();
        populateList(hotels, skyline);
        populateMap(hotels, skyline);
    });

    $('input[name="inputOpt"]', '#uiOpts').change(function() {
        var inputOpt = $('input[name="inputOpt"]:checked', '#uiOpts').val();
        if( inputOpt == "implicit" ) {
            $("input[name='skyline-opt']").hide()
                .parent()
                .toggleClass("checkbox");
        } else {
            $("input[name='skyline-opt']").show()
                .parent()
                .toggleClass("checkbox");
        }
        resetFilters();
    });


    $( "#colosseum-range" ).slider({
        range: true,
        step: 1,
        min: 0,
        max: 40,
        disabled: true,
        values: [ 0, 40 ],
        slide: function( event, ui ) {
            $("#colosseum-lab").text(ui.values[ 0 ] + " - " + ui.values[ 1 ] + " km");
        }
    }).on("slidechange", getHotels);

    setupRangeCheckbox( "colosseum" );

    $( "#trevi-range" ).slider({
        range: true,
        step: 1,
        min: 0,
        max: 40,
        disabled: true,
        values: [ 0, 40 ],
        slide: function( event, ui ) {
            $("#trevi-lab").text(ui.values[ 0 ] + " - " + ui.values[ 1 ] + " km");
        }
    }).on("slidechange", getHotels);
    setupRangeCheckbox( "trevi" );

    $( "#proximity-range" ).slider({
        range: true,
        step: 1,
        min: 0,
        max: 25,
        disabled: true,
        values: [ 0, 25 ],
        slide: function( event, ui ) {
            $("#proximity-lab").text(ui.values[ 0 ] + " - " + ui.values[ 1 ] + " km");
        }
    }).on("slidechange", getHotels);
    setupRangeCheckbox( "proximity" );

    $( "#rate-range" ).slider({
        range: true,
        min: 0,
        step: 10,
        max: 1300,
        disabled: true,
        values: [ 0, 1300 ],
        slide: function( event, ui ) {
            $("#rate-lab").text(ui.values[ 0 ] + " - " + ui.values[ 1 ] + " €");
        }
    }).on("slidechange", getHotels);
    setupRangeCheckbox( "rate" );

    $( "#stars-range" ).slider({
        range: "max",
        min: 1,
        max: 5,
        step: 1,
        disabled: true,
        slide: function( event, ui ) {
            $("#stars-lab").text(ui.value);
        }
    }).on("slidechange", getHotels);
    setupRangeCheckbox( "stars" );

    $( "#tripad-range" ).slider({
        range: "max",
        min: 0,
        max: 5,
        step: 0.5,
        disabled: true,
        slide: function( event, ui ) {
            $("#tripad-lab").text(ui.value);
        }
    }).on("slidechange", getHotels);
    setupRangeCheckbox( "tripad" );

    $( "#expedia-range" ).slider({
        range: "max",
        min: 0,
        max: 5,
        step: 0.5,
        disabled: true,
        slide: function( event, ui ) {
            $("#expedia-lab").text(ui.value);
        }
    }).on("slidechange", getHotels);
    setupRangeCheckbox( "expedia" );
    
    $("#map_canvas").height($("#option-pane").outerHeight() - $("#listMapTaps").outerHeight(true));
    if(gmap == undefined) {
        gmap = new google.maps.Map(d3.select("#map_canvas").node(), {
            zoom: 12,
            center: new google.maps.LatLng(41.9000, 12.5000),
            zoomControlOptions: {
                style: google.maps.ZoomControlStyle.LARGE
            },
            mapTypeId: google.maps.MapTypeId.ROADMAP
        });
    }

    $('#tab_map_link').on('shown', function (e) {
        google.maps.event.trigger(gmap, 'resize');
        if(hotels.length > 0) gmap.panTo(new google.maps.LatLng(hotels[0].lat, hotels[0].lon));
    });


    // Get an initial list of hotels
    getHotels();
 
});
