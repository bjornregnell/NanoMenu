object NanoMenu {
  //paste this object in the scala REPL and type NanoMenu.test()
  import java.awt._
  import java.awt.event._
  import javax.swing._
  import javax.swing.tree._
  import javax.swing.event._
  import KeyEvent._
  import ActionEvent.{CTRL_MASK => CTRL, ALT_MASK => ALT}

  trait MenuTree  
  case class MenuBranch(name: String, mnemonic: Int, menus: MenuTree*) extends MenuTree {
    def addTo(parent: JComponent): Map[String, JComponent] = {
      var menuMap: Map[String, JComponent] = Map()
      def iter(parent: JComponent, menus: Seq[MenuTree]): Unit = menus.foreach( _ match {
        case m: MenuLeaf => 
          val jmi = new JMenuItem(m.name, m.shortcut)
          jmi.addActionListener(m.action)
          if (m.accelerator>0) jmi.setAccelerator(KeyStroke.getKeyStroke(m.accelerator, m.mask))
          parent.add(jmi)
          menuMap += m.name -> jmi
        case m: MenuBranch =>
          val jm = new JMenu(m.name)
          if (m.mnemonic>0) jm.setMnemonic(mnemonic)
          parent.add(jm)
          menuMap += m.name -> jm
          iter(jm, m.menus)
      } )
      iter(parent, Seq(this))
      menuMap
    }
  }
  case class MenuLeaf(name: String, shortcut: Int, accelerator: Int, mask: Int)(block: => Unit) 
  extends MenuTree {
    def action = new ActionListener {
      def actionPerformed(e: ActionEvent) = block
    }
  }
  case class AppMenus(menus: MenuBranch*) {
    def addTo(frame: JFrame): Map[String, JComponent]  = {
      val menuBar = new JMenuBar()
      frame.setJMenuBar(menuBar)
      menus.map(_.addTo(menuBar)).reduceLeft(_ ++ _)
    }
  }

  object test {
    def apply() = {
      val menus = AppMenus(
        MenuBranch("File", VK_F,
            MenuLeaf("Open ...", VK_O, VK_O, CTRL){ println("open") },
            MenuLeaf("Save ...", VK_S, VK_S, CTRL){ println("save") }),
        MenuBranch("Edit", VK_E,
            MenuLeaf("Cut ...", VK_X, VK_X, CTRL){ println("cut") },
            MenuLeaf("Copy ...", VK_C, VK_C, CTRL){ println("copy") },
            MenuLeaf("Paste ...", VK_V, VK_V, CTRL){ println("paste") }),
        MenuBranch("Multi", VK_M,
            MenuBranch("first", VK_F,
                MenuLeaf("A ...", VK_A, 0, 0){ println("A") },
                MenuLeaf("B ...", VK_B, 0, 0){ println("B") }),
            MenuBranch("second", VK_S,
                MenuLeaf("C ...", VK_C, 0, 0){ println("C") },
                MenuLeaf("D ...", VK_D, 0, 0){ println("D") }))
      )
      val frame = new JFrame("nanomenu.test()")
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
      val menuMap = menus.addTo(frame)
      frame.pack()
      frame.setVisible(true)
      menuMap
    }
  }

}
