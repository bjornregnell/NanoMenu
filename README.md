NanoMenu
========

Minimalistic helper classes for concisely making a swing JMenuBar in scala.

Usage:

```scala
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
      menus.addTo(frame)
      frame.pack()
      frame.setVisible(true)
      frame   
```