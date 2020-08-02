package av3.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import av3.dao.json.RecipeDAO;
import av3.model.Ingredient;
import av3.model.Recipe;
import av3.view.listRecipes.ListRecipesJPanel;
import av3.view.registerRecipe.RegisterRecipe;

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Font;

public class MainJFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2051569066204438844L;
	private JPanel contentPane;
	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private ListRecipesJPanel listRecipesJPanel = new ListRecipesJPanel();
	private RegisterRecipe registerRecipePanel = new RegisterRecipe();

	private final JButton btnNew = new JButton("New");
	private final JButton btnEdit = new JButton("Edit");
	private final JButton btnDelete = new JButton("Delete");

	private final JButton btnSave = new JButton("Save");
	private final JButton btnCancel = new JButton("Cancel");
	private final JButton btnAddIngr = new JButton("Add Ingr.");
	private final JButton btnRemove = new JButton("Remove");

	private boolean isEditing = false;
	private RecipeDAO recipeDAO = new RecipeDAO();

	/**
	 * Getters and setters
	 */
	public ListRecipesJPanel getListRecipesJPanel() {
		return listRecipesJPanel;
	}

	public void setListRecipesJPanel(ListRecipesJPanel listRecipesJPanel) {
		this.listRecipesJPanel = listRecipesJPanel;
	}

	public JFrame getMainJFrame() {
		return this;
	}
	
	public boolean isEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}

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
	 * Creates the frame.
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
		tabbedPane.add("Register", registerRecipePanel);
		tabbedPane.setEnabledAt(1, false);

		handleButtons();
		new TableEvents();
		tabbedPane.setSelectedIndex(0);
	}

	private void handleButtons() {
		new ListRecipesButtonHandler();
		new RegisterRecipesButtonHandler();
	}

	private void changeTabFromTo(int from, int to) {
		tabbedPane.setSelectedIndex(to);
		tabbedPane.setEnabledAt(from, false);
		tabbedPane.setEnabledAt(to, true);
	}
	
	private void fillRegisterRecipeTab() {
		
		JTable tableRecipes = listRecipesJPanel.getTable();
		int selectedRow = tableRecipes.getSelectedRow();
		String selectedTitle = tableRecipes.getValueAt(selectedRow, 0).toString();
		Recipe foundRecipe = recipeDAO.findByName(selectedTitle);
		
		if (foundRecipe == null) {
			return;
		}
		registerRecipePanel.setTitle(foundRecipe.getTitle());
		registerRecipePanel.setAuthor(foundRecipe.getAuthor());
		registerRecipePanel.setHowTo(foundRecipe.getHowTo());
		registerRecipePanel.setIngredients(foundRecipe.getIngredients());
	}

	private void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void editingRoutine() {
		setEditing(true);
		changeTabFromTo(0, 1);
		fillRegisterRecipeTab();
	}
	
	private class ListRecipesButtonHandler{
		
		public ListRecipesButtonHandler() {
			handleButtonNew();
			handleButtonDelete();
			handleButtonEdit();
		}
		
		private void handleButtonNew() {
			btnNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					changeTabFromTo(0, 1);
					setEditing(false);
				}
			});
			btnNew.setBounds(370, 109, 89, 23);
			listRecipesJPanel.add(btnNew);
		}
		
		private void handleButtonEdit() {
			btnEdit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editingRoutine();
				}
			});
			btnEdit.setEnabled(false);
			btnEdit.setBounds(370, 143, 89, 23);
			listRecipesJPanel.add(btnEdit);
		}
		
		private void handleButtonDelete() {
			btnDelete.addActionListener(new ActionListener() {
				
				JTable tableRecipes = listRecipesJPanel.getTable();
				
				public void actionPerformed(ActionEvent arg0) {
					int selectedRow = tableRecipes.getSelectedRow();
					String recipeTitle = tableRecipes.getValueAt(selectedRow, 0).toString();
					recipeDAO.remove(recipeTitle);
					listRecipesJPanel.reloadData();
				}
			});
			btnDelete.setEnabled(false);
			btnDelete.setBounds(370, 178, 89, 23);
			listRecipesJPanel.add(btnDelete);
		}	
	}
	
	private class RegisterRecipesButtonHandler {

		public RegisterRecipesButtonHandler(){
			handleButtonSave();
			handleButtonCancel();
			handleButtonAddIngredient();
			handleButtonRemoveIng();
		}
		
		private void handleButtonSave() {
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (!registerRecipePanel.doSaveAction()) {
						showErrorMessage("Title, Author, HowTo and Ingredients are required fields!");
						return;
					}
					String title = registerRecipePanel.getTitle();
					String author = registerRecipePanel.getAuthor();
					String howTo = registerRecipePanel.getHowTo();
					ArrayList<Ingredient> ingredients = registerRecipePanel.getIngredients();
					
					if (isEditing()) {
						
						JTable tableRecipes = listRecipesJPanel.getTable();
						int selectedRow = tableRecipes.getSelectedRow();
						String oldTitle = tableRecipes.getValueAt(selectedRow, 0).toString();
						
						Object [] data = {title, author,howTo,ingredients};
						recipeDAO.updateRecipe(oldTitle, data);
					
					} else {
						
						Recipe recipe = new Recipe(title, author, howTo, ingredients);
						recipeDAO.insert(recipe);
					}
					
					registerRecipePanel.clearRecipeData();
					listRecipesJPanel.reloadData();
					changeTabFromTo(1, 0);
					setEditing(false);
				}
			});
			btnSave.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnSave.setBounds(370, 24, 89, 23);
			registerRecipePanel.add(btnSave);
		}
		
		private void handleButtonCancel() {
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					registerRecipePanel.clearRecipeData();
					changeTabFromTo(1, 0);
					setEditing(false);
				}
			});
			btnCancel.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnCancel.setBounds(370, 58, 89, 23);
			registerRecipePanel.add(btnCancel);
		}
		
		private void handleButtonAddIngredient() {
			btnAddIngr.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					registerRecipePanel.getDadosIngredientes().addRow(new Object[] {});
				}
			});
			btnAddIngr.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnAddIngr.setBounds(370, 305, 89, 23);
			registerRecipePanel.add(btnAddIngr);
		}
		
		private void handleButtonRemoveIng() {
			btnRemove.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					JTable tableIngredients = registerRecipePanel.getTableIngredients();
					int selectedRow = tableIngredients.getSelectedRow();
					int modelRow = tableIngredients.convertRowIndexToModel(selectedRow);
					registerRecipePanel.getDadosIngredientes().removeRow(modelRow);
				}
			});
			btnRemove.setEnabled(false);
			btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnRemove.setBounds(370, 335, 89, 23);
			registerRecipePanel.add(btnRemove);
		}

	}
	
	private class TableEvents {
		
		public TableEvents() {
			onTableIngredientsSelectionChange();
			onTableRecipesListSelectionChange();
			onRecipeListDoubleClick();
		}
		
		public void onTableIngredientsSelectionChange() {

			JTable tableIngredients = registerRecipePanel.getTableIngredients();
			tableIngredients.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent event) {
					boolean alguemSelecionado = tableIngredients.getSelectedRow() != -1;
					btnRemove.setEnabled(alguemSelecionado);
				}

			});
		}
		
		private void onTableRecipesListSelectionChange() {

			JTable tableRecipes = listRecipesJPanel.getTable();
			tableRecipes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent event) {
					boolean alguemSelecionado = tableRecipes.getSelectedRow() != -1;
					btnDelete.setEnabled(alguemSelecionado);
					btnEdit.setEnabled(alguemSelecionado);
				}

			});
		}
		
		private void onRecipeListDoubleClick() {
			listRecipesJPanel.getTable().addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if(me.getClickCount() == 2) {
						editingRoutine();
					}
				}
			});
		}

	}
	
}
