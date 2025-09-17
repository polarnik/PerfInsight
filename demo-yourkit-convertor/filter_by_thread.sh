#!/bin/zsh

START_LINE='                                                        <node call_tree="GapRestServlet.kt:85 &lt;...&gt; org.glassfish.jersey.servlet.WebComponent.service(URI, URI, HttpServletRequest, HttpServletResponse)"'
STOP_LINE='                                                        </node>'

awk -v start_line="$START_LINE" -v stop_line="$STOP_LINE"  'BEGIN { web=0; stack_trace_number=0; }
{
  if ( index($0, start_line)==1 ) {
    web=1;
    stack_trace_number++;
    spacesep=index($0, "<")
    spaces=substr($0, 1, spacesep-1)
    space_count=length(spaces)
  }
  if (web==1) {
    printf("%-10s", stack_trace_number);
    print substr($0, space_count, length($0))
    if ( index($0, stop_line)==1 ) {
      web=0
    }
  }
}' ./build/calls/Call-tree-by-thread.xml > ./build/calls/Call-tree-by-thread-WEB.xml
