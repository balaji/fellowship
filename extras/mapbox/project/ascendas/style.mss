

Map { background-color: lighten(#fff1cf, 7); }

#indoor::l[building = "yes"] { 
  line-color: black;
  line-width: 2;
  }

#indoor::l[buildingpart = "shelf"] {
  line-color: black;
  line-width: 1.5;
  polygon-fill: gray; 
  }

#indoor::l[buildingpart = "shelf"][zoom < 20] {
  line-width: 0.5;  
 }

#indoor::l[building = "yes"][zoom < 20] { 
  line-width: 1;
}

#indoor::l[highway="service"],
#indoor::l[barrier="wall"]{
  line-color: black;
  line-width: 3;
  }

.border::highlight {
   line-color: #3F6;
}