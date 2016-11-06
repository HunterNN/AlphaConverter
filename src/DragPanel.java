import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

public class DragPanel extends JPanel implements DropTargetListener {

	public DragPanel() {
		DropTarget drop_target = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this, true);
	}

	@Override
	public void dragEnter(DropTargetDragEvent drop_event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragExit(DropTargetEvent drop_event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent drop_event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drop(DropTargetDropEvent drop_event) {
		drop_event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		Transferable transferable = drop_event.getTransferable();
		if(transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
			try{
				Object data = transferable.getTransferData(DataFlavor.javaFileListFlavor);
				if(data instanceof List){
					for(Object value : ((List) data)){
						if(value instanceof File){
							File file = (File) value;
							performFileAction(file);
						}
					}
				}
			} catch(UnsupportedFlavorException | IOException ex){
				ex.printStackTrace();
			}
		}
	}
	
	public void performFileAction(File file){}

	@Override
	public void dropActionChanged(DropTargetDragEvent drop_event) {
		// TODO Auto-generated method stub

	}

}
