import javax.swing.JFrame;


public class appFrame extends JFrame{
    
    appFrame(){
        this.setTitle("Messaging App");
        this.add(new appPanel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setFocusable(true);
        this.pack();
    }


}
