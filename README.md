<h1> PacMan-In-Multiverse</h1>

<p>In this project, I have refactored existing codebase given by Arcade 24™ (A24). Who had made a simple version of PacMan Game to an extension: <b>PacMan-In-Multiverse</b></p>

<img width="591" alt="image" src="https://github.com/Sanskar-Agarwal/PacMan-In-Multiverse/assets/86827884/98a78c63-536d-43af-a1c3-10da2a90de61">


<h2> Extensions </h2>

<li>Refactoring the existing code to meet software engineering design principles,as a well-designed model makes design and implementation of the proposed extensions
much simpler. I have used GoF and GRASP Design Principles, a report has been added highlighting key design decisions taken and their justification.
<li>Addition of <b>new monsters</b> and <b>item behaviours</b> as per specifications which have been elaborated below
<li>Provision of <b>Autoplayer Mode</b>, where PacMan moves randomly to try and complete the game, and <b>Player Mode</b> where one can play on their own.

<h3>Monsters</h3>

<p>existing monsters have the following walking approach</p>

<img width="671" alt="image" src="https://github.com/Sanskar-Agarwal/PacMan-In-Multiverse/assets/86827884/08d5ceee-0e1c-471d-9629-c6b149ab4ec3">

<p><b>New Monsters</b> that were proposed by A24 and implemented by me, having the following walking approach</p>

<img width="680" alt="image" src="https://github.com/Sanskar-Agarwal/PacMan-In-Multiverse/assets/86827884/1638b6cf-1663-48b6-8013-bef6c03a4a44">

<h3>Item Behaviours</h3>

<p>Capabilities were also requested to alter behaviour of PacMan and Monsters in response to pacman eating <ins> gold or ice pieces</ins> </p>

<li><b>Furious Mode:</b> When PacMan eats a gold piece, all the monsters get furious and move fasters. The monsters
determine the moving direction once based on their walking approach and move towards that
direction for 2 cells if they can. Otherwise, determining the new direction again using their own
walking approach until it can move by 2 cells. After 3 seconds, all the monsters will be back to move
normally using their own walking approach.</li>
<li><b>Freeze:</b> Regardless of being normal or furious, all the monsters are frozen (i.e., stop moving) for 3
seconds. Then, they will be back to move normally using their own walking approach.While the monsters are frozen, PacMan can eat a gold piece without making the monsters
furious.</li>

<h2>Running the codebase and further instructions</h2>

<p>The main entry point to run this code is <b>"src.Driver.main()"</b>
  
  I have built this project on IntelliJ IDEA CE and have used 'openjdk16', however the code should run without using an integrated development environment (IDE) as well.
  
  On IntelliJ -> Open Projects -> 'PacMan-In-Multiverse' -> 'PacMan Game' -> Open 

  You will need to set up an SDK for your project. Right click on your project, and select Open Module Settings. In module settings we need to choose our sdk, I have used 'openjdk16'


  <img width="539" alt="image" src="https://github.com/Sanskar-Agarwal/PacMan-In-Multiverse/assets/86827884/6b1e7727-4d78-4511-8c69-f7edd5a88764">


<img width="1026" alt="image" src="https://github.com/Sanskar-Agarwal/PacMan-In-Multiverse/assets/86827884/7ba2fa2e-2167-454c-b6ea-3017f153b1b1">

Associated JGameGrid Library can be accessed <a href="https://www.aplu.ch/home/apluhomex.jsp?site=45" target="_blank">JGameGrid</a> with further reading on classes and their framework on <a href="https://www.aplu.ch/classdoc/jgamegrid/ch/aplu/jgamegrid/package-summary.html" target="_blank">Javadoc</a>

</p>










