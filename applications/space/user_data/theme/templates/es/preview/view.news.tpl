@content
::content::

@content.news$post.titleLinkTask
<a class="command" href="showtask::view|view::(::target::::view|,*::)">::title::</a>

@content.news$post.titleLinkNode
<a class="command" href="shownode(::target::)">::title::</a>

@content.news$post
<li id="post_::postId::" class="postlist">
  <!--<div class="contextual">
    <ul>
      <li><a class="command" href="addfilter(post_::postId::,::postId::,MESSAGE)">No mostrar más este mensaje</a></li>
      <li><a class="command" href="addfilter(post_::postId::,::postId::,AUTHOR)">No mostrar más mensajes de este autor</a></li>
    </ul>
  </div>-->
  <div class="posticon ::image::"></div>
  <div>
    <h6 class="messageTitle">::messageTitle::</h6>
    <span class="messageBody">::messageBody::</span>
    <div class="messageFooter">
      <span class="createdate">::createDate::</span>
    </div>
    <ul class="commentlist">
      ::comments::
      <li class="newcomment disable">
        <div>
            <textarea title="Escribe un comentario..." name="add_comment_text_text">Escribe un comentario...</textarea>
            <a class="command button" href="addcommenttopost(::postId::)">Enviar</a>
        </div>
      </li>
    </ul>
  </div>
</li>  

@content.news$comment
<li class="comment">
  <div>
    <div class="icon"></div>
    <div class="commentContent">
      <span class="actorName" style="font-weight: bold;">::author::</span> <span>::commentBody::</span>
      <div class="commentActions" style="font-size: 11px; color: gray;">::createDate::</div>
    </div>
  </div>
</li>
