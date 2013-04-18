"use strict";

var hotels;


function populateList(hs) {
    console.log("Populating list");
    var items = "";
    var hlist = $("#hotel-list")
    hlist.empty();
    $(hs.slice(0,50)).each(function(index, item) {
        hlist.append(
            $(document.createElement('li'))
                .attr("class", "media")
                .html(htmlHotel(item))
        );
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

function htmlHotel(hotel){
    var imgP = /low|high/.test(hotel.picture) ? 
        'assets/data/'+hotel.picture : "assets/data/hotel_images/high/na.jpg"

    return '<img class="media-object pull-left" src="'+imgP+'">'+
        '<div class="media-body"><h4 class="media-heading">'+hotel.name+'</h4>'+
        '<p>'+hotel.address1+'<br />'+
        hotel.postalCode+' '+hotel.city+'<br />'+
        'High Rate: '+hotel.highRate.toFixed(2)+'€<br />'+
        '</p></div>';
}


var gmap;

function getHotels() {
    var colRange = $( "#colosseum-range" ).slider("values");
    var treviRange = $( "#trevi-range" ).slider("values");
    var proxRange = $( "#proximity-range" ).slider("values");
    var rateRange = $( "#rate-range" ).slider("values");
    var expedia =  $( "#expedia-range" ).slider("value");
    var tripad =  $( "#tripad-range" ).slider("value");

    console.log(rateRange);
    jQuery.getJSON("/skyline_hotels", 
                   {highRateStart: rateRange[0],
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
                    tripAdvisorRatingStart: tripad == 0 ? -1 : tripad,
                   },
                   function(hs) {
                       hotels = hs;
                       populateList(hs.slice(0,100));
                   });
}

$(function() {
    $('#tab_map_link').on('shown', function (e) {
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
    });
    
    $(window).resize(function () {
        var h = $(window).height(),
            offsetTop = 190; // Calculate the top offset
        $('#map_canvas').css('height', (h - offsetTop));
    }).resize();


    $(':checkbox').change(getHotels);

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

    // Get an initial list of hotels
    getHotels();
 
});
