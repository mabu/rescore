package rescore.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class ComboModel implements ComboBoxModel {

    private List data = new ArrayList();
    private int selected = 0;

    public ComboModel(List list)
    {
       data = list;
    }

    public void setSelectedItem(Object o)
    {
       selected = data.indexOf(o);
    }

    public Object getSelectedItem()
    {
       return data.get(selected);
    }

    public int getSize()
    {
       return data.size();
    }

    public Object getElementAt(int i)
    {
       return data.get(i);
    }
    

	public void addListDataListener(ListDataListener arg0)
	{
	}

	public void removeListDataListener(ListDataListener arg0)
	{
	}

}
