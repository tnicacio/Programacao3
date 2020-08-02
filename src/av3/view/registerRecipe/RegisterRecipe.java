package av3.view.registerRecipe;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import av3.model.Ingredient;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTable;
import java.awt.Rectangle;
import java.util.ArrayList;

public class RegisterRecipe extends JPanel {
	
	private static final long serialVersionUID = -9194741463310260158L;
	
	private JTextField textFieldTitle;
	private JTextField textFieldAuthor;
	private JTable tableIngredients;
	private JTextArea howToArea;

	private DefaultTableModel dadosIngredientes;
	private String[] columnNames = { "Qty.", "Unit", "Name" };
	private TableRowSorter<DefaultTableModel> rowSorter;
	private JScrollPane scrollPaneHowTo;
	private JScrollPane scrollPaneIngredients;
	
	
	/**
	 * Getters and setters
	 */
	public JTable getTableIngredients() {
		return this.tableIngredients;
	}
	
	public DefaultTableModel getDadosIngredientes() {
		return (DefaultTableModel) this.tableIngredients.getModel();
	}

	public String getTitle() {
		return textFieldTitle.getText();
	}

	public void setTitle(String title) {
		this.textFieldTitle.setText(title);
	}

	public String getAuthor() {
		return textFieldAuthor.getText();
	}

	public void setAuthor(String author) {
		this.textFieldAuthor.setText(author);
	}
	
	public String getHowTo() {
		return howToArea.getText();
	}
	public void setHowTo(String howTo) {
		this.howToArea.setText(howTo);
	}

	public ArrayList<Ingredient> getIngredients() {
		if (tableIngredients.getRowCount() == 0) {
			return null;
		}
		ArrayList<Ingredient> ingredients = new ArrayList<>();
		for (int i = 0; i < tableIngredients.getRowCount(); i++) {
			
			Double quantity = Double.parseDouble(tableIngredients.getValueAt(i, 0).toString());
			String unit = tableIngredients.getValueAt(i, 1).toString();
			String name = tableIngredients.getValueAt(i, 2).toString();
			
			Ingredient ingredient = new Ingredient(name, unit, quantity);
			ingredients.add(ingredient);
		}
		return ingredients;
	}
	
	public void setIngredients(ArrayList<Ingredient> ingredients) {
		//Clear the previous data
		dadosIngredientes.setRowCount(0);	
		if (ingredients == null) {
			return;
		}
		//Add the ingredients to the table
		for (Ingredient i : ingredients) {
			dadosIngredientes.addRow(new Object[] {
					i.getQuantity(),i.getUnit(), i.getName()
			});
		}
	}
	
	public JScrollPane getScrollPaneHowTo() {
		return this.scrollPaneHowTo;
	}
	public JScrollPane getScrollPaneIngredients() {
		return this.scrollPaneIngredients;
	}
	
	
	/**
	 * Creates the panel.
	 */
	public RegisterRecipe() {
		setBounds(new Rectangle(0, 0, 50, 480));
		setLayout(null);
		createsTitleInputField();
		createsAuthorInputField();
		createsHowToAreaWithScroll();
		createsIngredientTableWithScrollAndRowSorter();
	}
	
	public void clearRecipeData() {
		setTitle("");
		setAuthor("");
		setHowTo("");
		setIngredients(null);
	}
	
	public boolean doSaveAction() {
			
		if (tableIngredients.getRowCount() == 0) {
			return false;
		}
		for (int i = 0; i < tableIngredients.getRowCount(); i++) {	
			if (tableIngredients.getValueAt(i, 0) == null || tableIngredients.getValueAt(i, 0).toString().trim().equals("") ||
				tableIngredients.getValueAt(i, 1) == null || tableIngredients.getValueAt(i, 1).toString().trim().equals("") ||
				tableIngredients.getValueAt(i, 2) == null || tableIngredients.getValueAt(i, 2).toString().trim().equals("")) {
				return false;
			}
		}
		return !getTitle().trim().equals("") && !getAuthor().trim().equals("") && !getHowTo().trim().equals("");
	}
	
	private void createsTitleInputField() {
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTitle.setBounds(25, 29, 46, 14);
		add(lblTitle);

		textFieldTitle = new JTextField();
		textFieldTitle.setColumns(10);
		textFieldTitle.setBounds(67, 26, 292, 20);
		add(textFieldTitle);
	}
	
	private void createsAuthorInputField() {
		JLabel lblAuthor = new JLabel("Author");
		lblAuthor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAuthor.setBounds(25, 57, 46, 14);
		add(lblAuthor);

		textFieldAuthor = new JTextField();
		textFieldAuthor.setColumns(10);
		textFieldAuthor.setBounds(67, 54, 292, 20);
		add(textFieldAuthor);
	}
	
	private void createsHowToAreaWithScroll() {
		JLabel lblHowTo = new JLabel("How to do it");
		lblHowTo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblHowTo.setBounds(25, 98, 111, 14);
		add(lblHowTo);

		scrollPaneHowTo = new JScrollPane();
		scrollPaneHowTo.setAutoscrolls(true);
		scrollPaneHowTo.setBounds(25, 124, 334, 139);
		add(scrollPaneHowTo);

		howToArea = new JTextArea();
		howToArea.setAutoscrolls(false);
		scrollPaneHowTo.setViewportView(howToArea);
	}
	
	private void createsIngredientTableWithScrollAndRowSorter() {
		
		JLabel lblIngredients = new JLabel("Ingredients");
		lblIngredients.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblIngredients.setBounds(25, 280, 92, 14);
		add(lblIngredients);
		
		scrollPaneIngredients = new JScrollPane();
		scrollPaneIngredients.setBounds(25, 305, 334, 96);
		add(scrollPaneIngredients);

		dadosIngredientes = new DefaultTableModel(new Object[][] {}, columnNames) {

			private static final long serialVersionUID = 1855869488231749311L;
			boolean[] columnEditables = new boolean[] { true, true, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		rowSorter = new TableRowSorter<DefaultTableModel>(dadosIngredientes);
		tableIngredients = new JTable();
		tableIngredients.setModel(dadosIngredientes);
		tableIngredients.setAutoCreateRowSorter(true);
		tableIngredients.setRowSorter(rowSorter);
		TableColumn column = null;
		for (int i = 0; i < 3; i++) {
			column = tableIngredients.getColumnModel().getColumn(i);
			if (i > 1) {
				column.setPreferredWidth(250);
			} else {
				column.setPreferredWidth(90);
			}
		}
		for (int i = 0; i < tableIngredients.getColumnCount(); i++) {
			tableIngredients.getColumnModel().getColumn(i).setResizable(false);
		}
		scrollPaneIngredients.setViewportView(tableIngredients);
	}
	
}
