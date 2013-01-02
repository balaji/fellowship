@futura_med: "Futura Medium","Function Pro Medium","Ubuntu Regular","Trebuchet MS Regular","DejaVu Sans Book";

#indoor::l[point = "yes"] { 
  text-face-name: @futura_med;
  text-fill:magenta;
  text-halo-fill:rgba(255,255,255,0.5);
  text-halo-radius:3;
  text-line-spacing:1;
  text-name:"[name]";
}


#indoor::l[buildingpart = "shelf"] { 
  text-face-name: @futura_med;
  text-fill:blue;
  text-halo-fill:rgba(255,255,255,0.5);
  text-halo-radius:3;
  text-line-spacing:1;
  text-name:"[name]";
}

#indoor::l[point = "yes"][zoom = 19] {
 text-size:12; 
}

#indoor::l[point = "yes"][zoom = 20] {
 text-size:14; 
}

#indoor::l[point = "yes"][zoom = 21] {
 text-size:16; 
}

#indoor::l[buildingpart = "shelf"][zoom = 19] {
 text-size:10; 
}

#indoor::l[buildingpart = "shelf"][zoom > 20] {
 text-size:14; 
}