package av2.model;

public class Ingredient {

	private String name;
	private String unit;
	private double quantity;

	public Ingredient(String name, String unit, double quantity) {
		try {
			boolean existenceCondition = name != null && !name.trim().equals("") && unit != null
					&& !unit.trim().equals("") && quantity > 0;

			if (!existenceCondition) {
				throw new Error("Name, unit and quantity are ingredients required fields!");
			}
			
			this.name = name;
			this.unit = unit;
			this.quantity = quantity;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnit() {
		return this.unit;
	}
	
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getQuantity() {
		return this.quantity;
	}

	@Override
	public String toString() {
		String comma = ", ";
		return new StringBuilder()
				.append("{")
				.append("name: ").append(getName()).append(comma)
				.append("unit: ").append(getUnit()).append(comma)
				.append("quantity: ").append(getQuantity())
				.append("}")
				.toString();
	}

}
