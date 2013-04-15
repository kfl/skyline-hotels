"use strict";

var hotels;


function populateList(hs) {
    console.log("Populating list");
    d3.select("#hotel-list").selectAll("li")
        .data(hs)
        .enter()
          .append("li")
          .attr("class", "media")
          .call(listHotel);
}


function listHotel(div){
    console.log("Make hotel listing");
    div.append("img")
        .attr("class","media-object pull-left")
        .attr("src", function(h){return "assets/data/"+h.picture;}); 
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


d3.json("assets/data/hotel_data.json")
    .get(function(error, hs) {
        hotels = hs;
        populateList(hs.slice(0,100));
    });

var gmap;

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


$(function() {
    $( "#colosseum-range" ).slider({
        range: true,
        min: 0,
        max: 500,
        values: [ 75, 300 ],
        slide: function( event, ui ) {
            console.log(ui.values[ 0 ] + " - " + ui.values[ 1 ]);
            //$( "#amount" ).val( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );
        }
    });
    $( "#trevi-range" ).slider({
        range: true,
        min: 0,
        max: 500,
        values: [ 75, 300 ],
        slide: function( event, ui ) {
            console.log(ui.values[ 0 ] + " - " + ui.values[ 1 ]);
            //$( "#amount" ).val( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );
        }
    });
    $( "#proximity-range" ).slider({
        range: true,
        min: 0,
        max: 500,
        values: [ 75, 300 ],
        slide: function( event, ui ) {
            console.log(ui.values[ 0 ] + " - " + ui.values[ 1 ]);
            //$( "#amount" ).val( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );
        }
    });
    $( "#rate-range" ).slider({
        range: true,
        min: 0,
        max: 500,
        values: [ 75, 300 ],
        slide: function( event, ui ) {
            console.log(ui.values[ 0 ] + " - " + ui.values[ 1 ]);
            //$( "#amount" ).val( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );
        }
    });
    // $( "#amount" ).val( "$" + $( "#slider-range" ).slider( "values", 0 ) +
    //                     " - $" + $( "#slider-range" ).slider( "values", 1 ) );
});


// $('#ListMapTab a').click(function (e) {
//     e.preventDefault();
//     $(this).tab('show');
// });

// d3.tsv("data/hotel_data.tsv")
//     .row(function(h) {
//         return {
//             order: +h.order,
//             id: +h.id,
//             name: h.name,	
//             address1: h.address1,
//             city: h.city,
//             postalCode: h.postalCode,
//             propertyCategory: +h.propertyCategory,
//             hotelRating: +h.hotelRating,
//             confidenceRating: +h.confidenceRating,
//             tripAdvisorRating: +h.tripAdvisorRating,
//             highRate: +h.highRate,
//             lat: +h.lat,
//             lon: +h.long,
//             proximityDistance: +h.proximityDistance,
//             internet: h['Internet Access Available'] == 'Y' ? true : false,
//             pool: h.Pool == 'Y' ? true : false,
//             distFromColosseum: +h.DistFromColosseum,
//             distFromTreviFountain: +h.DistFromTreviFountain,
//             picture: h.picture
//         };
//     })
//     .get(function(error, hs) {
//         hotels = hs;
//         d3.select("body").append("pre").text(JSON.stringify(hotels));
//     });

