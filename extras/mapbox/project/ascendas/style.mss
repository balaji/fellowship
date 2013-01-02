Map { background-color: lighten(#fff1cf, 7); }

#indoor::l[building = "yes"] { 
  line-color: black;
  line-width: 2;
}

#indoor::l[building = "yes"][zoom < 20] { 
  line-width: 1;
}

#indoor::l[buildingpart = "shelf"] {
  line-color: black;
  polygon-fill: gray; 
}


#indoor::l[buildingpart = "cashier"] {
  line-color: black;
  polygon-fill: darken(gray, 20); 
}

#indoor::l[buildingpart = "shelf"][zoom = 19] {
  line-width: 1;
} 

#indoor::l[buildingpart = "shelf"][zoom = 20] {
  line-width: 2;  
}

#indoor::l[buildingpart = "shelf"][zoom = 21] {
  line-width: 3;  
}

#indoor::l[highway="service"],
#indoor::l[barrier="wall"]{
  line-color: black;
  line-width: 3;
}
