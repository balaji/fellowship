Map {
  background-color: #fff;
}


@purple:#ffffff;
@blue:#3b8394;
#ssn::l[amenity="college"]
{
  polygon-fill:@purple;
  }

#ssn::fill[zoom>=0] {
  [building="dept b"] { polygon-fill:@blue; }
  }