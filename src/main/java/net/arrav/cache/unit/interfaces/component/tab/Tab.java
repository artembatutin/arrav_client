package net.arrav.cache.unit.interfaces.component.tab;


import net.arrav.Client;
import net.arrav.cache.unit.interfaces.Interface;

/**
 * Handles all the Tab menu actions.
 */
public enum Tab {
	DEFAULT() {
		@Override
		public void selectOption(int tab, Client client, Interface parent, Interface tabID) {
			client.outBuffer.putOpcode(251);
			client.outBuffer.putInt(parent.id);
			client.outBuffer.putByte(tab);
			tabID.tab.setSelectedTab(tab);
		}
	},
	DEFAULT_SERVER() {
		@Override
		public void selectOption(int tab, Client client, Interface parent, Interface tabID) {
			client.outBuffer.putOpcode(251);
			client.outBuffer.putInt(parent.id);
			client.outBuffer.putByte(tab);
		}
	},

	EQUIPMENT_TAB() {
		@Override
		public void selectOption(int option, Client client, Interface parent, Interface tabID) {
			tabID.tab.setSelectedTab(option);
			modifyChild(19001, 4, option == 0 ? 19005 : 19006);

		}
	},
	//QUEST_TAB() {
	//	@Override
	//	public void selectOption(int tab, Client client, Interface parent, Interface tabID) {
//
	//		switch (tab) {
	//			case 0:
	//				modifyChild(PARENT, CONTAINER_FRAME, INFORMATION_TAB);
	//				break;
	//			case 1:
	//				modifyChild(PARENT, CONTAINER_FRAME, FUNCTIONAL);
	//				break;
	//			case 4:
	//				if(client.donator != PlayerRights.NONE) {
	//					client.outBuffer.putOpcode(251);
	//					client.outBuffer.putInt(parent.interfaceId);
	//					client.outBuffer.putByte(tab);
	//				} else
	//					client.pushMessage("You must be a donator to access this tab.", 0, "");
	//				return;
	//		}
	//		tabID.tab.setSelectedTab(tab);
	//	}
	//},
	BANK() {
		@Override
		public void selectOption(int tab, Client client, Interface parent, Interface tabID) {
			Client.instance.currentBankTab = tab;
			tabID.tab.setSelectedTab(tab);
			Interface.cache[5385].children[0] = 270 + tab;
		}
	};
	Tab() {}
	public abstract void selectOption(int option, Client client, Interface parent, Interface tabID);

	public void modifyChild(int parent, int frameID, int newChild) {
		Interface parentInterface = Interface.cache[parent];
		if(parentInterface == null || frameID == -1) {
			System.out.println("can't change child p:"+parent+" f:"+frameID+" n:"+newChild);
			return;
		}
			parentInterface.children[frameID] = newChild;
	}

	public void toggleChild(int parent, boolean show) {
		Interface parentInterface = Interface.cache[parent];
		if(parentInterface == null) {
			System.out.println("can't toggle child p:"+parent);
			return;
		}
		//parentInterface.interfaceShown = show;

	}
}
