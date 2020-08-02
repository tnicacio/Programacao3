package av2.view.listRecipes;

import javax.swing.JPanel;

import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import av3.dao.json.RecipeDAO;
import av3.model.Recipe;

import java.awt.Rectangle;

public class ListRecipesJPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7582915657897832936L;
	private JTextField textField;
	private JTable table;
	private DefaultTableModel dadosTabela;
	private TableRowSorter<DefaultTableModel> rowSorter;
	private String [] columnNames = {"Title","Author"};
	private RecipeDAO recipeDAO = new RecipeDAO();
	
	
	public JTable getTable() {
		return table;
	}
	
	public DefaultTableModel getDadosTabela() {
		return (DefaultTableModel) this.table.getModel();
	}
	
	public RecipeDAO getRecipeDAO() {
		return this.recipeDAO;
	}

	/**
	 * Create the panel.
	 */
	public ListRecipesJPanel() {
		setBounds(new Rectangle(0, 0, 500, 480));
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Localizar Receita");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel.setBounds(25, 11, 85, 15);
		add(lblNewLabel);
		
		textField = new JTextField();
		textField.setToolTipText("Busque sua receita preferida");
		textField.setColumns(10);
		textField.setBounds(25, 36, 334, 20);
		textField.getDocument().addDocumentListener(
				new DocumentListener() {		
					@Override
					public void removeUpdate(DocumentEvent e) {
						newFilter();		
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						newFilter();
					}
					
					@Override
					public void changedUpdate(DocumentEvent e) {
						newFilter();
						
					}
				}
			);
		add(textField);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 76, 334, 320);
		add(scrollPane);
		
		
		dadosTabela = new DefaultTableModel(new Object[][] {},columnNames) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
					false, false, false
					};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		rowSorter = new TableRowSorter<DefaultTableModel>(dadosTabela);
		table = new JTable();
		table.setModel(dadosTabela);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setResizable(false);
		}
		table.setBorder(new BevelBorder(BevelBorder.LOWERED,null,null,null,null));
		table.setAutoCreateRowSorter(true);
		table.setRowSorter(rowSorter);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						int viewRow = table.getSelectedRow();
						if (viewRow >= 0) {
							int modelRow = table.convertRowIndexToModel(viewRow);
                    		System.out.println(String.format("Selected Row in view: %d. " +
                          "Selected Row in model: %d.", 
                          viewRow, modelRow));
						}
					}
					
				}
		);
		scrollPane.setViewportView(table);
		this.populate();
	}
	
	private void newFilter() {
	    RowFilter<DefaultTableModel, Object> rf = null;
	    try {
	        rf = RowFilter.regexFilter("(?i)" + textField.getText(), 0);
	    } catch (java.util.regex.PatternSyntaxException e) {
	        e.printStackTrace();
	    }
	    rowSorter.setRowFilter(rf);
	}
	
	public void populate() {
		if (recipeDAO.listRecipes().isEmpty()) {
			return;
		}
		for (Recipe r : recipeDAO.listRecipes()) {
			dadosTabela.addRow(new Object[] {r.getTitle(),r.getAuthor()});
		}
	}
	
	public void reloadData() {
		dadosTabela.setRowCount(0);
		populate();
	}
	
}
