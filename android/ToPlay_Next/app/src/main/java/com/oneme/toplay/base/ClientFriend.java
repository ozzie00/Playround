package com.oneme.toplay.base;

/*
import im.tox.jtoxcore.ToxFriend;
import im.tox.jtoxcore.ToxUserStatus;
*/

//
//Class that represents a Friend in Playround. When things are stored to local disk
//apart from the internal Tox data, the id should be used as a key, as this is
//the only part of the data that is not subject to change.
//
//Offers capability of adding a personal Nickname for someone and keeping track
//of their previous names
//
public class ClientFriend { //  implements ToxFriend {

    /*
	//
	//Fields for internal Tox stuff. Fields marked as transient should not be
	//stored in local storage, as they are handled by jToxcore itself.
	//
	private transient int friendNumber;
	private String friendID;
	private transient String friendName;
	private transient ToxUserStatus status;
	private transient String statusMessage;
	private transient boolean online;
    private transient boolean isTyping;

	//
	//Fields for playround-specific implementation. These should be stored in local
	//storage
	//
	private String nickname;
	private ArrayList<String> previousNames;

	//
	//Default constructor for a Friend. Apart from creating friends in
	//{@link im.tox.jtoxcore.JTox}, there should be no need to invoke this manually.
	//
	//@param friendnumber
	//           friendnumber for the newly created friend
	//
	public ClientFriend(int friendnumber) {
		this.friendNumber = friendnumber;
		this.status       = ToxUserStatus.TOX_USERSTATUS_NONE;
		this.online       = false;
	}

	@Override
	public int getFriendnumber() {
		return this.friendNumber;
	}

	@Override
	public String getId() {
		return this.friendID;
	}

	@Override
	public String getName() {
		return this.friendName;
	}

	@Override
	public ToxUserStatus getStatus() {
		return this.status;
	}

	@Override
	public String getStatusMessage() {
		return this.statusMessage;
	}

	@Override
	public boolean isOnline() {
		return this.online;
	}

    @Override
    public boolean isTyping() {
        return this.isTyping;
    }

	//
	//@return this friend's nickname
	//
	public String getNickname() {
		return this.nickname;
	}

	//
	//@return this friend's previous nicknames
	//
	public ArrayList<String> getPreviousNames() {
		return this.previousNames;
	}

	@Override
	public void setId(String id) {
		this.friendID = id;
	}

	@Override
	public void setName(String name) {
		this.friendName = name;
	}

	@Override
	public void setOnline(boolean online) {
		this.online = online;
	}

	@Override
	public void setStatus(ToxUserStatus status) {
		this.status = status;
	}

	@Override
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

    @Override
    public void setTyping(boolean isTyping) {
        this.isTyping = isTyping;
    }

       //
       //@param nickname
       //           the new nickname for this friend
       //
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	//
	//
	//@param previousNames
        //       list of previous usernames
	//
	public void setPreviousNames(ArrayList<String> previousNames) {
		this.previousNames = previousNames;
	}
	*/
}
