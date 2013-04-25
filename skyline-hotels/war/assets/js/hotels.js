"use strict";

var hotels;


function populateList(hs) {
    console.log("Populating list");
    var hlist = $("#hotel-list");

    $(hs.slice(0,50)).each(function(index, item) {
        var hotelElm = $(document.createElement('li'))
            .attr("class", "media")
            .html(htmlHotel(item));
        var details = hotelDetails(item);
        $(".btn", hotelElm).popover({placement: 'left', html:true, content: details});
        hlist.append(hotelElm);
    });

    
    
    // var listings = d3.select("#hotel-list").selectAll("li")
    //     .data(hs, function(h) { return h.id; });

    // listings.enter()
    //       .append("li")
    //       .attr("class", "media")
    //       .call(listHotel);
    // listings.exit()
    //       .remove();
}

function listHotel(div){
    console.log("Make hotel listing");
    div.append("img")
        .attr("class","media-object pull-left")
        .attr("src", function(h) {
            return /low|high/.test(h.picture) ? 
                'assets/data/'+h.picture : "assets/data/hotel_images/high/na.jpg";
        }); 
    div.append("div")
        .attr("class","media-body")
        .each(function(d){
            d3.select(this)
                .append("h4")
                .attr("class","media-heading")
                .text(d.name);
            d3.select(this)
                .append("p")
                .text(d.address1);
        });
}


var debug_flag = false; 
function debug_output(str) {
    return debug_flag ? str : '';
}

function htmlHotel(hotel){
    var imgP = /low|high/.test(hotel.picture) ? 
        'assets/data/'+hotel.picture : "assets/data/hotel_images/high/na.jpg";
    return '<img class="media-object pull-left img-polaroid" src="'+imgP+'">'+
        '<div class="media-body"><h4 class="media-heading">'+hotel.name+'</h4>'+
        '<p>'+hotel.address1+
        '<a data-original-title="'+hotel.name+'" href="#" class="btn pull-right" data-toggle="popover" '+
        'data-trigger="click"><i class="icon-info-sign"></i> Details</a>'+
        '<br />'+
        hotel.postalCode+' '+hotel.city+'<br />'+
        'Price per night: '+hotel.highRate.toFixed(2)+'€<br />'+
        debug_output('(id: '+hotel.id+', order: '+hotel.order+')')+
        '</p></div>';
}


function hotelDetails (hotel) {
    var imgP = /low|high/.test(hotel.picture) ? 
        'assets/data/'+hotel.picture : "assets/data/hotel_images/high/na.jpg";
    return '<img class="details-img pull-left img-polaroid" src="'+imgP+'">'+
        '<div class="media-body"><h4 class="media-heading">'+hotel.name+'</h4>'+
        '<p>'+hotel.address1+', '+hotel.postalCode+' '+hotel.city+'<br />'+
        'Price per night: '+hotel.highRate.toFixed(2)+'€<br />'+
        'Pool: '+ (hotel.pool ? 'Yes' : 'No') + ', '+
        'Internet: '+ (hotel.pool ? 'Yes' : 'No') + '<br />'+
        'Distance From Colosseum: '+hotel.distFromColosseum.toFixed(2)+', '+
        'Distance From Trevi Fountain: '+hotel.distFromTreviFountain.toFixed(2)+', '+
        'Distance to City Center: '+hotel.proximityDistance.toFixed(2)+'<br />'+
        'Expedia Rating: '+hotel.hotelRating.toFixed(1)+', '+
        'Trip Advisor Rating: '+hotel.tripAdvisorRating.toFixed(1)+''+
        '</p>';
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

function getRange( selector ) {
    var values = $(selector).slider("values");
    var options = $(selector).slider("option");
    values[0] = values[0] == options.min ? undefined : values[0];
    values[1] = values[1] == options.max ? undefined : values[1];
    return values;
}

function getHotels() {

    clearList();
    showAlert('<h4>Getting Data</h4>Kick back and put your feet up while we work.',
              'alert-info');

    var colRange   = getRange( "#colosseum-range" );
    var treviRange = getRange( "#trevi-range" );
    var proxRange  = getRange( "#proximity-range" );
    var rateRange  = getRange( "#rate-range" );
    var expedia    = $( "#expedia-range" ).slider("value");
    var tripad     = $( "#tripad-range" ).slider("value");
    var sorting    = $( "#sortby" ).val();
    var query = {highRateStart: rateRange[0],
                 highRateEnd: rateRange[1],
                 proximityDistanceStart: proxRange[0],
                 proximityDistanceEnd: proxRange[1],
                 distFromColosseumStart: colRange[0],
                 distFromColosseumEnd: colRange[1],
                 distFromTreviFountainStart: treviRange[0],
                 distFromTreviFountainEnd: treviRange[1],
                 pool: $("#pool").is(':checked'),
                 internet: $("#internet").is(':checked'),
                 hotelRatingStart: expedia == 0 ? -1 : expedia,
                 tripAdvisorRatingStart: tripad == 0 ? -1 : tripad
                };

    if (sorting != "default" ) {
        var parts = sorting.split("-");
        query['sortBy'] = parts[0];
        query['sortType'] = parts[1];

        if (sorting == "skyline") {
            var satt = $.map($("input[name='skyline-opt']:checked"), function(e,i){
                return e.value;
            });
            if (satt.length > 0) query['skylineOf'] = satt.join(',');
        }
    } 

    jQuery.getJSON("/skyline_hotels",
                   query,
                   function(hs) {
                       hotels = hs;
                       clearList();
                       populateList(hs);
                       populateMap(hs);
                   })
        .fail(function ( jqxhr, textStatus, error ) {
            clearList();
            var err = jqxhr.status + ' ' + textStatus + ': ' + error;
            showAlert('<h4>Oh snap, something went wrong</h4>'+err,
                      'alert-error');
        });
}


var gmap;
var markersArray = [];

function addHotelMarker(hotel) {
    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(hotel.lat, hotel.lon),
        map: gmap,
        title: name
    });

    var infowindow = new google.maps.InfoWindow({
        content: hotelDetails(hotel)
    });

    google.maps.event.addListener(marker, 'click', function() {
        infowindow.open(gmap,marker);
    });

    markersArray.push(marker);
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

function populateMap(hs) {
    console.log("Populating map");
    deleteHotelOverlays()
    $(hs).each(function(index, hotel) {
        addHotelMarker(hotel);
    });
}


$(function() {

    $('#internet,#pool').change(getHotels);
    $("#sortby").change(getHotels);

    $("input[name='skyline-opt']").change(function () {
        if($( "#sortby" ).val() == 'skyline')
            getHotels();
    });

    $( "#colosseum-range" ).slider({
        range: true,
        step: 1,
        min: 0,
        max: 40,
        values: [ 0, 40 ],
        change: getHotels,
        slide: function( event, ui ) {
            $("#colosseum-lab").text(ui.values[ 0 ] + " - " + ui.values[ 1 ] + " km");
        }
    });
    $( "#trevi-range" ).slider({
        range: true,
        step: 1,
        min: 0,
        max: 40,
        values: [ 0, 40 ],
        change: getHotels,
        slide: function( event, ui ) {
            $("#trevi-lab").text(ui.values[ 0 ] + " - " + ui.values[ 1 ] + " km");
        }
    });
    $( "#proximity-range" ).slider({
        range: true,
        step: 1,
        min: 0,
        max: 25,
        values: [ 0, 25 ],
        change: getHotels,
        slide: function( event, ui ) {
            $("#proximity-lab").text(ui.values[ 0 ] + " - " + ui.values[ 1 ] + " km");
        }
    });
    $( "#rate-range" ).slider({
        range: true,
        min: 0,
        step: 10,
        max: 5250,
        values: [ 0, 5250 ],
        change: getHotels,
        slide: function( event, ui ) {
            $("#rate-lab").text(ui.values[ 0 ] + " - " + ui.values[ 1 ] + " €");
        }
    });

    $( "#tripad-range" ).slider({
        range: "max",
        min: 0,
        max: 5,
        step: 0.5,
        change: getHotels,
        slide: function( event, ui ) {
            $("#tripad-lab").text(ui.value);
        }
    });

    $( "#expedia-range" ).slider({
        range: "max",
        min: 0,
        max: 5,
        step: 0.5,
        change: getHotels,
        slide: function( event, ui ) {
            $("#expedia-lab").text(ui.value);
        }
    });
    
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
    });

    // $(window).resize(function () {
    //     var h = $(window).height(),
    //         offsetTop = 190; // Calculate the top offset
    //     $('#map_canvas').css('height', (h - offsetTop));
    // }).resize();



    // Get an initial list of hotels
    getHotels();
 
});
