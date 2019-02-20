<?php
  $myDirectory = opendir(".");

  while($entryName = readdir($myDirectory)) {
	$dirArray[] = $entryName;
  }

  closedir($myDirectory);

  $indexCount	= count($dirArray);

  sort($dirArray);


  $url_beta = '';
  $url_stable = '';
  for($index=0; $index < $indexCount; $index++) {    
    if (substr("$dirArray[$index]", 0, 1) != ".") { // don't list hidden files
      if (filetype($dirArray[$index]) == "dir") {
        $url_stable = $url_beta;
        $url_beta = "http://". $_SERVER['HTTP_HOST'] .dirname($_SERVER['REQUEST_URI'])."/$dirArray[$index]";
      }
    } 
  }

  $op = $_GET['op'];
  switch ($op) {
    case "beta": print($url_beta); break;
    case "stable": print($url_stable); break;
  }
?> 
