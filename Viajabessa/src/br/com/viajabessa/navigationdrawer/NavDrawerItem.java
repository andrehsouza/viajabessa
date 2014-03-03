package br.com.viajabessa.navigationdrawer;

public class NavDrawerItem {
    
    private String title;
    private int icon;
    private boolean selected;
     
    public NavDrawerItem(){}
 
    public NavDrawerItem(String title, int icon, boolean selected){
        this.title = title;
        this.icon = icon;
        this.selected = selected;
    }
     
    public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getTitle(){
        return this.title;
    }
     
    public int getIcon(){
        return this.icon;
    }
     
    public void setTitle(String title){
        this.title = title;
    }
     
    public void setIcon(int icon){
        this.icon = icon;
    }
}