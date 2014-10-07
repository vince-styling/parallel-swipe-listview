Parallel-Swipe-ListView
=======================

This project was born to answer this stackoverflow [question](http://stackoverflow.com/questions/25946509/how-to-control-viewpages-pages-from-another-page/25968469). We are trying to reproduce an interesting animation which origin from Xiaomi the `Contacts` application.

![animation_live](/animation_live.gif "animation_live")

But I'm failed to making it fully like the origin's, below is my effect :

![animation_live_so_far](/animation_live_so_far.gif "animation_live_so_far")

In the original implementation, we saw the animation also happened to the ListView's divider. I've tried to achieve this but I found ListView didn't expose the `drawDivider` method, which means we can't manipulate the divider's drawing. But it doesn't matter, even if ListView does, another problem is ListView didn't open a method like `invalidateDivier()` to performing redraw the dividers. We can't take every divider like the way we take its items, make us never be control the divider's **translationX**. I thought Xiaomi might be redesign the Android SDK then refactor the ListView and so many widgets like that to making them more easier to interpose its behaviors. But I cannot prove it, because I be aware of the Xiaomi SDK doesn't open its sources while I seeking a Class named `miui.v5.view.ViewPager`. I've made my conclusion at last : the dividers manipulation can't be achieve.

In my implementation, I use `PageTransformer` to apply my animation thanks for above question's answer. According to the original animation, it is easy to find that the ListView's items adjustment only happen with the coming pages. Whatever the direction how to change, the starting page never feel. So inside the `transformPage(View page...)` method, my first priority is recognizing that the **page** is really the starting page or others. Especially swiping through three pages and the direction change frequently. Given current arguments in **transformPage**, I can't recognize the starting page accurately. So I make each Fragment carrying their INDEX via `setTag()` and persisting ViewPager's CURRENT_POSITION once its scroll state came idle. Also preventing following touch events while ViewPager is settling its pages.

Now, my effort can work, but I just think it wasn't elegant enough. Maybe as long as my experience grow strong I will know some optimize way then make it better.


































