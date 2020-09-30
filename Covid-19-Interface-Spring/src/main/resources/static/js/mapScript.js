  var radiusInfVal = function(value) {
        var rtnNum = 500;
        if(value == 0){
            rtnNum
        } else if(value > 0 && value < 20){
            rtnNum = rtnNum * 5;
        }else if(value > 20 && value < 100){
            rtnNum = rtnNum * 10;
        }else if(value > 100 && value < 300){
            rtnNum = rtnNum * 20;
        }else if(value > 300 && value < 800){
            rtnNum = rtnNum * 40;
        }else if(value > 800 && value < 2000){
            rtnNum = rtnNum * 60;
        }else if(value > 2000 && value < 5000){
            rtnNum = rtnNum * 70;
        }else if(value > 5000 && value < 10000){
            rtnNum = rtnNum * 100;
        }else if(value > 10000 && value <20000) {
            rtnNum = rtnNum * 200;
        }else if(value > 20000 && value < 40000){
            rtnNum = rtnNum * 250;
        }else if(value > 40000 && value < 60000){
            rtnNum = rtnNum * 300;
        }else if(value > 60000){
            rtnNum = rtnNum * 400;
        }
        return rtnNum;
    }
//var countryDetails = [[${countryDetails}]];
var map = L.map('mapid').setView([51.9194,19.1451], 5);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);
//w tym csv problem z francja, zle koordynaty.
    countryDetails.forEach(country => {
        if(!country.province || country.province.length === 0) {
            L.circle([country.lat, country.lon], {
                color: 'red',
                fillColor: '#f03',
                fillOpacity: 0.5,
                radius: radiusInfVal(country.infected)})
                .addTo(map)
                .bindPopup(country.countryName + "<br>"+ 'Infected: ' + country.infected + "<br>" +
                'Killed: ' + country.killed + "<br>" +
                    'Recovered: ' + country.recovered + "<br>" +
                    'Last update: ' + country.lastReleaseDate);
        } else {
        L.circle([country.lat, country.lon], {
                        color: 'red',
                        fillColor: '#f03',
                        fillOpacity: 0.5,
                        radius: radiusInfVal(country.infected)})
                        .addTo(map)
                        .bindPopup("Province/State: "+ country.province + "<br>" + country.countryName + "<br>"+ 'Infected: ' + country.infected + "<br>" +
                        'Killed: ' + country.killed + "<br>" +
                            'Recovered: ' + country.recovered + "<br>" +
                            'Last update: ' + country.lastReleaseDate);
        }
            });