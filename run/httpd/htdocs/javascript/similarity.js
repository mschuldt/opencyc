function calc_checkbox_change () {
 chkchng=document.SimilarityForm['cb-similarity-assert'];
 chkchng.value='';
 for (i = 0; i< document.SimilarityForm.length; i++) {
   the_box = document.SimilarityForm.elements[i];
   if (the_box.type == 'checkbox' &&
       the_box.checked != the_box.defaultChecked) {
       if (chkchng.value != '')
         chkchng.value += ' ';
       chkchng.value += the_box.name;
       }
   }
}

function reset_elements () {
  for (i = 0; i < document.SimilarityForm.length; i++) {
    the_element = document.SimilarityForm.elements[i];
    if (the_element.type == 'checkbox')
      the_element.checked = the_element.defaultChecked;
    }
  for (i = 0; i < document.images.length; i++) {
    the_image=document.images[i];
    if (the_image.name.substr(0,3) == 'st|')
      the_image.src='/cycdoc/img/cb/white.gif'
    }
}

function set_row (row_name, type) {
 document.images[row_name].src='/cycdoc/img/cb/' + type + '.gif';
 for (i = 0; i< document.SimilarityForm.length; i++) {
   the_box = document.SimilarityForm.elements[i];
   if (the_box.type == 'checkbox' &&
       the_box.name.substr(0,row_name.length) == row_name) {
      switch (type) {
        case 'green' :
          the_box.checked = true;
          break;
        case 'red' :
          the_box.checked = false;
          break;
        case 'white' :
          the_box.checked = the_box.defaultChecked;
          break;
        }
     }
  }
}

function toggle_row (row_name) {
row_type=document.images[row_name].src;
row_type=row_type.substr(row_type.lastIndexOf('/') + 1);
row_type=row_type.substr(0,row_type.length - 4);
 switch (row_type) {
   case 'white' : 
     set_row(row_name, 'green');
     break;
   case 'green' :
     set_row(row_name, 'red');
     break;
   default :
     set_row(row_name, 'white');
     break;
   }
}
function customize (row_name) {
cnt = 0;
on = 0;
off = 0;
def = 0;
 for (i = 0; i< document.SimilarityForm.length; i++) {
   the_box = document.SimilarityForm.elements[i];
   if (the_box.type == 'checkbox' &&
       the_box.name.substr(0,row_name.length) == row_name) {
         cnt++;
         if (the_box.checked == true)
           on++;
         else
           off++;
         if (the_box.checked == the_box.defaultChecked)
           def++;
        }
     }
 the_image = document.images[row_name]
 if (cnt == def)
   the_image.src='/cycdoc/img/cb/white.gif';
 else if (cnt == off)
   the_image.src='/cycdoc/img/cb/red.gif';
 else if (cnt == on)
   the_image.src='/cycdoc/img/cb/green.gif';
 else
   the_image.src='/cycdoc/img/cb/blue.gif'; 
}
