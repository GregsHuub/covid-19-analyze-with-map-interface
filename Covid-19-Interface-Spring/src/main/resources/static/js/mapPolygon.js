
//var countryDetails = [[${countryDetails}]];
var map = L.map('mapid').setView([51.9194,19.1451], 5);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);
//koordynaty z dupy z tego Omanu
    polygonDetails.forEach(point => {
    console.log(point.msisdn)
    L.circle([point.location_lat, point.location_lon], {
                            color: 'red',
                            fillColor: '#f03',
                            fillOpacity: 0.5,
                            radius: 10})
                            .addTo(map)
                            .bindPopup("msisdn: "+ point.msisdn);
    })
.bindPopup("msisdn Number: TEST polygon");
var polygon = L.polygon([
    [51.600, -0.12],
    [51.509, -0.08],
    [51.503, -0.06],
    [51.51, -0.047]
]).addTo(map);