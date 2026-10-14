import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class App extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainContainer;

    public App() {
        setTitle("Tic-Tac-Toe");
        setSize(620, 900);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        Menu mainMenu = new Menu(vsCpu -> {
            Game game = new Game(vsCpu, () -> {
                cardLayout.show(mainContainer, "MENU");
            });
            
            mainContainer.add(game, "GAME");
            cardLayout.show(mainContainer, "GAME");
        });

        mainContainer.add(mainMenu, "MENU");
        add(mainContainer);
        setVisible(true);
    }
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            new App().setVisible(true);
        });
    }
}
