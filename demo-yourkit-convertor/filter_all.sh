#!/bin/zsh

START_LINE='<node call_tree="GapRestServlet.kt:85 org.glassfish.jersey.servlet.WebComponent.service(URI, URI, HttpServletRequest, HttpServletResponse)"'

FIRST_LINE='<view description="Call tree (all threads merged)">'
LAST_LINE='</view>'

awk -v start_line="$START_LINE" -v first_line="$FIRST_LINE" -v last_line="$LAST_LINE" 'BEGIN {
  web=0;
  stop_line="";
  print first_line;
}
{
  if ( index($0, start_line)>0 ) {
    web=1;
    spacesep=index($0, "<");
    spaces=substr($0, 1, spacesep-1);
    space_count=length(spaces);
    stop_line=spaces "</node>";
  }
  if (web==1) {
    print substr($0, space_count, length($0))
    if ( index($0, stop_line)==1 ) {
      web=0;
      exit;
    }
  }
}
END {
  print last_line;
}' ./build/calls/Call-tree-all-threads-merged.xml > ./build/calls/Call-tree-all-threads-merged-WEB.xml

