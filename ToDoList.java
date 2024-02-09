import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

class Task extends JPanel{
	JLabel index;
	JTextField taskName;
	JButton done;
	
	Color white = new Color(255,255,255);
	Color green = new Color(188,226,158);
	Color doneColor = new Color(188,226,158);
	
	private boolean checked;
	
	Task(){
		this.setPreferredSize(new Dimension(400,20)); //size of the app
		this.setBackground(white); //set background color of the app
		this.setLayout(new BorderLayout()); // layout
		
		checked = false;
		
		index = new JLabel(""); // creates index label
		index.setPreferredSize(new Dimension(20,20)); // size of index label
		index.setHorizontalAlignment(JLabel.CENTER); // alignment of index label
		this.add(index, BorderLayout.WEST); // add index label to task
		
		taskName = new JTextField(); // Create task name
		taskName.setText("Enter Something");
		taskName.addFocusListener(new FocusListener(){
			@override
			public void focusGained(FocusEvent e){
				if(taskName.getText().equals("Enter Something")){
					taskName.setText("");
				}
			}
			
			@override
			public void focusLost(FocusEvent e){
				if(taskName.getText().equals("")){
					taskName.setText("Enter Something");
				}
			}
		});
		taskName.setBorder(BorderFactory.createEmptyBorder()); //removes border
		taskName.setBackground(white); // text background color
		this.add(taskName, BorderLayout.CENTER);
		
		done = new JButton("Done");
		done.setPreferredSize(new Dimension(80, 20));
		done.setBorder(BorderFactory.createRaisedBevelBorder());
		done.setBackground(doneColor);
		done.setFocusPainted(false);
		this.add(done, BorderLayout.EAST); 
	}
	
	public void changeIndex(int num){
		this.index.setText(num + ""); // change num to string
		this.revalidate(); // refreshes
	}
	
	public JButton getDone(){
		return done;
	}
	
	public boolean getState(){
		return checked;
	}
	
	public void changeState(){
		this.setBackground(green);
		taskName.setBackground(green);
		checked = true;
		revalidate();
	}
}

class List extends JPanel{
	Color lightColor = new Color(252, 221, 176);
	List(){
		GridLayout layout = new GridLayout(10, 1);
		layout.setVgap(5); // vertical gap
		
		this.setLayout(layout); // 10 tasks
		this.setPreferredSize(new Dimension(400, 560));
		this.setBackground(lightColor);
	}
	
	public void updateNumbers(){
		Component[] listItems = this.getComponents();
		
		for(int i = 0; i<listItems.length; i++){
			if(listItems[i] instanceof Task){
				((Task) listItems[i]).changeIndex(i + 1);
			}
		}
	}
	
	public void removeCompletedTasks(){
		for(Component c : getComponents()){
			if(c instanceof Task){
				if(((Task)c).getState()){
					remove(c); // removes the completed task
					updateNumbers();
				}
			}
		}
	}
}

class Footer extends JPanel{
	JButton addTask;
	JButton clear;
	
	Color orange = new Color(233,133,128);
	Color lightColor = new Color(252,221,176);
	Border emptyBorder = BorderFactory.createRaisedBevelBorder();
	
	Footer(){
		this.setPreferredSize(new Dimension(400,60));
		this.setBackground(lightColor);
		
		addTask = new JButton("Add Task"); //task button
		addTask.setBorder(emptyBorder); // remove border
		addTask.setFont(new Font("Sans-serif", Font.ITALIC, 20)); // Font
		addTask.setVerticalAlignment(JButton.BOTTOM); // align
		addTask.setBackground(orange); // Background color
		this.add(addTask); // Footer
		this.add(Box.createHorizontalStrut(20)); // Space Between buttons
		
		clear = new JButton("Clear finished tasks");// Clear tasks Button
		clear.setFont(new Font("Sans-serif", Font.ITALIC, 20)); // Font
		clear.setBorder(emptyBorder); // Removes Border
		clear.setBackground(orange); // Backgound Color
		this.add(clear); // add to footer
	}
	
	public JButton getNewTask(){
		return addTask;
	}
	
	public JButton getClear(){
		return clear;
	}
}

class TitleBar extends JPanel{
	Color lightColor = new Color(252,221,176);
	
	TitleBar(){
		this.setPreferredSize(new Dimension(400,80)); // size of title bar
		this.setBackground(lightColor); // title bar color
		JLabel titleText = new JLabel("To-Do-List"); // text in tile bar
		titleText.setPreferredSize(new Dimension(200, 60)); // size of titlebar text
		titleText.setFont(new Font("Sans-serif", Font.BOLD,20)); // Font type
		titleText.setHorizontalAlignment(JLabel.CENTER); // Allignment of the text of titlebar
		this.add(titleText); // add the text to titlebar command
	}
}

class AppFrame extends JFrame {

    private TitleBar title;
    private Footer footer;
    private List list;

    private JButton newTask;
    private JButton clear;

    AppFrame() {
        this.setSize(400, 600); // 400 width and 600 height
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
        this.setVisible(true); // Make visible

        title = new TitleBar();
        footer = new Footer();
        list = new List();

        this.add(title, BorderLayout.NORTH); // Add title bar on top of the screen
        this.add(footer, BorderLayout.SOUTH); // Add footer on bottom of the screen
        this.add(list, BorderLayout.CENTER); // Add list in middle of footer and title

        newTask = footer.getNewTask();
        clear = footer.getClear();

        addListeners();
    }
    
	public void addListeners(){
		newTask.addMouseListener(new MouseAdapter(){
			@override
			public void mousePressed(MouseEvent e){
				Task task = new Task();
				list.add(task); // add new task to the list above
				list.updateNumbers(); // upates the task
				
				task.getDone().addMouseListener(new MouseAdapter(){
					@override
					public void mousePressed(MouseEvent e){
						task.changeState(); // change task color
						list.updateNumbers(); // updates number of tasks
						revalidate(); // updates the app
					}
				});
			}
		});
		
		clear.addMouseListener(new MouseAdapter(){
			@override
			public void mousePressed(MouseEvent e){
				list.removeCompletedTasks(); // removes all the tasks that is done
				repaint(); // repaints the list
			}
		});
	}
}

public class ToDoList{
	public static void main(String args[]){
		AppFrame frame = new AppFrame(); // Creates the frame
	}
}

@interface override{

}
