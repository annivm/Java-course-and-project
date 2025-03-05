package viikko9;

public class CategoryFilter extends EventFilter {
    private Category category;

    public CategoryFilter(Category category) {
        this.category = category;
    }

    public Category getCategory(){
        return this.category;
    }

    public boolean accepts(Event event){
        Category c = (Category) event.getCategory();
        if (c.getSecondary() == null &&
            c.getPrimary().equals(this.category.getPrimary())){
            return true;
        }
        if (c.getPrimary().equals(this.category.getPrimary()) &&
            c.getSecondary().equals(this.category.getSecondary())){
            return true;
        }
        return false;
    }
}
