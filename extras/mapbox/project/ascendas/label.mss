@futura_med: "Futura Medium","Function Pro Medium","Ubuntu Regular","Trebuchet MS Regular","DejaVu Sans Book";
@futura_italic: "Futura Medium Italic","Function Pro Medium Italic","Ubuntu Italic","Trebuchet MS Italic","DejaVu Sans Oblique";
@futura_bold: "Futura Bold","Function Pro Bold","Ubuntu Bold","Trebuchet MS Bold","DejaVu Sans Bold";

#indoor::l[buildingpart = "shelf"] { 
  text-face-name: @futura_med;
  text-fill:brown;
  text-size:9;
  text-halo-fill:rgba(255,255,255,0.5);
  text-halo-radius:3;
  text-line-spacing:1;
  text-name:"[name]";
 }


#indoor::l[point = "yes"] { 
  text-face-name: @futura_med;
  text-fill:magenta;
  text-size:12;
  text-halo-fill:rgba(255,255,255,0.5);
  text-halo-radius:3;
  text-line-spacing:1;
  text-name:"[name]";
 }

#indoor::l[buildingpart = "shelf"][zoom = 20] { 
  text-face-name: @futura_bold;
  text-fill:brown;
  text-size:12;
 }


#indoor::l[buildingpart = "shelf"][zoom = 21] { 
  text-face-name: @futura_bold;
  text-fill:brown;
  text-size:15;
 }
