package av2.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import av2.view.listRecipes.ListRecipesJPanel;

import javax.swing.JTabbedPane;

public class MainJFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2051569066204438844L;
	private JPanel contentPane;
	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private ListRecipesJPanel listRecipesJPanel = new ListRecipesJPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainJFrame frame = new MainJFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 100, 500, 500);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.add("Recipes", listRecipesJPanel);
	}

	public ListRecipesJPanel getListRecipesJPanel() {
		return listRecipesJPanel;
	}

	public void setListRecipesJPanel(ListRecipesJPanel listRecipesJPanel) {
		this.listRecipesJPanel = listRecipesJPanel;
	}

	public JFrame getMainJFrame() {
		return this;
	}
	
}
