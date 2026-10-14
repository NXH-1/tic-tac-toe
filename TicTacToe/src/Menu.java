import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;

public class Menu extends JPanel {

    public Menu(Consumer<Boolean> onPlayClicked) {
        setBackground(Theme.CUSTOM_DARK_GRAY);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 15, 15, 15);


        JLabel titleLabel = new JLabel("Tic-Tac-Toe");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setForeground(Theme.TEXT_COLOR);
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(Theme.CUSTOM_DARK_GRAY);
        optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));

        JRadioButton vsFriendButton = new JRadioButton("vs Friend", true);
        JRadioButton vsCpuButton = new JRadioButton("vs CPU", false);

        ButtonGroup group = new ButtonGroup();
        group.add(vsFriendButton);
        group.add(vsCpuButton);

        styleRadioButton(vsFriendButton);
        styleRadioButton(vsCpuButton);

        optionsPanel.add(vsFriendButton);
        optionsPanel.add(vsCpuButton);

        gbc.gridy = 1;
        add(optionsPanel, gbc);

        // Play Button
        JButton playButton = new JButton("Play");
        playButton.setFont(Theme.BUTTON_FONT);
        playButton.setBackground(Theme.CUSTOM_GREEN);
        playButton.setForeground(Theme.CUSTOM_DARK_GRAY);
        playButton.setFocusable(false);
        playButton.setPreferredSize(new Dimension(200, 50));

        playButton.addActionListener(e -> {
            boolean vsCpu = vsCpuButton.isSelected();
            onPlayClicked.accept(vsCpu);
        });

        gbc.gridy = 2;
        gbc.insets = new Insets(35, 15, 15, 15);
        add(playButton, gbc);

        setVisible(true);
    }

    private void styleRadioButton(JRadioButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Theme.TEXT_COLOR);
        button.setBackground(Theme.CUSTOM_DARK_GRAY);
        button.setFocusable(false);
    }
}