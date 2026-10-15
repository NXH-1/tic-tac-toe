import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.*;

public class Menu extends JPanel {
    private boolean vsCpu = false;
    private float currentX;
    private float targetX;
    private float currentWidth;
    private float targetWidth;
    private Timer slideTimer;

    public Menu(Consumer<Boolean> onPlayClicked) {
        setBackground(Theme.CUSTOM_DARK_GRAY);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 15, 15, 15);

        JLabel titleLabel = new JLabel("Tic-Tac-Toe");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 80));
        titleLabel.setForeground(Theme.TEXT_COLOR);
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JLabel vsFriendButton = new JLabel("Friend", JLabel.CENTER);
        JLabel vsCpuButton = new JLabel("CPU", JLabel.CENTER);

        vsFriendButton.setFont(Theme.BUTTON_FONT);
        vsCpuButton.setFont(Theme.BUTTON_FONT);

        vsFriendButton.setForeground(Color.WHITE);
        vsCpuButton.setForeground(Theme.SUB_TEXT_COLOR);
        vsFriendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        vsCpuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        vsFriendButton.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        vsCpuButton.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel selectedContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Theme.CUSTOM_BLUE);
                g2d.fillRoundRect((int) currentX, 4, (int) currentWidth, getHeight() - 8, 30, 30);
            }
        };
        selectedContainer.setOpaque(false);
        selectedContainer.setLayout(new GridLayout(1, 2, 0, 0));
        selectedContainer.setPreferredSize(new Dimension(440, 45));
        selectedContainer.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        slideTimer = new Timer(15, e -> {
            if (Math.abs(targetX - currentX) < 0.5f) {
                currentX = targetX;
                currentWidth = targetWidth;
                slideTimer.stop();
            } else {
                currentX += (targetX - currentX) * 0.25f;
                currentWidth += (targetWidth - currentWidth) * 0.25f;
            }
            selectedContainer.repaint();
        });

        Runnable updateSelection = () -> {
            if (!vsCpu) {
                vsFriendButton.setForeground(Color.WHITE);
                vsCpuButton.setForeground(Theme.SUB_TEXT_COLOR);
                targetX = vsFriendButton.getX();
                targetWidth = vsFriendButton.getWidth();
            } else {
                vsCpuButton.setForeground(Color.WHITE);
                vsFriendButton.setForeground(Theme.SUB_TEXT_COLOR);
                targetX = vsCpuButton.getX();
                targetWidth = vsCpuButton.getWidth();
            }
            slideTimer.start();
        };

        vsFriendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vsCpu = false;
                updateSelection.run();
            }
        });

        vsCpuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vsCpu = true;
                updateSelection.run();
            }
        });

        selectedContainer.add(vsFriendButton);
        selectedContainer.add(vsCpuButton);

        gbc.gridy = 1;
        add(selectedContainer, gbc);

        // Play Button
        JButton playButton = new JButton("Play");
        playButton.setFont(Theme.BUTTON_FONT);
        playButton.setBackground(Theme.CUSTOM_GREEN);
        playButton.setForeground(Theme.CUSTOM_DARK_GRAY);
        playButton.setFocusable(false);
        playButton.setPreferredSize(new Dimension(200, 50));

        playButton.addActionListener(e -> onPlayClicked.accept(vsCpu));

        gbc.gridy = 2;
        gbc.insets = new Insets(35, 15, 15, 15);
        add(playButton, gbc);

        SwingUtilities.invokeLater(() -> {
            currentX = vsFriendButton.getX();
            currentWidth = vsFriendButton.getWidth();
            targetX = currentX;
            targetWidth = currentWidth;
            selectedContainer.repaint();
        });
    }
}