package com.oneme.toplay.base;


import com.oneme.toplay.Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/*
import im.tox.jtoxcore.FriendExistsException;
import im.tox.jtoxcore.FriendList;
import im.tox.jtoxcore.ToxFriend;
import im.tox.jtoxcore.ToxUserStatus;
*/

public class ClientFriendList { // implements FriendList<ClientFriend> {
    /*
    private final String TAG = "ClientFriendList";

    //
	//Underlying friend list
	//
	private List<ClientFriend> friends;

	//
	//Create a new, empty ToxFriendList instance
	//
	public ClientFriendList() {
		this.friends = Collections
				.synchronizedList(new ArrayList<ClientFriend>());
	}

	//
	//Create a new ToxFriendList instance with the given List of Friends as an
	//underlying structure
	//
	//@param friends
	//           initial list of friends to use
	//
	public ClientFriendList(ArrayList<ClientFriend> friends) {
		this.friends = friends;
	}

	@Override
	public ClientFriend getByFriendNumber(int friendnumber) {
		synchronized (this.friends) {
			for (ClientFriend friend : this.friends) {
				if (friend.getFriendnumber() == friendnumber) {
					return friend;
				}
			}
		}
		return null;
	}

	@Override
	public ClientFriend getById(String id) {
        if(id!=null) {
            synchronized (this.friends) {
                for (ClientFriend friend : this.friends) {

                    if (Application.APPDEBUG) {
                        Log.d(TAG, " friend " + friend.getId());
                    }

                    if (id.equals(friend.getId())) {
                        return friend;
                    }
                }
            }
        }
		return null;
	}

	@Override
	public List<ClientFriend> getByName(String name, boolean ignorecase) {
		if (ignorecase) {
			return getByNameIgnoreCase(name);
		}

		ArrayList<com.oneme.toplay.base.ClientFriend> result = new ArrayList<com.oneme.toplay.base.ClientFriend>();
		synchronized (this.friends) {
			for (com.oneme.toplay.base.ClientFriend f : this.friends) {
				if (name == null && f.getName() == null) {
					result.add(f);
				} else if (name != null && name.equals(f.getName())) {
					result.add(f);
				}
			}
		}
		return result;
	}

	private List<ClientFriend> getByNameIgnoreCase(String name) {
		ArrayList<com.oneme.toplay.base.ClientFriend> result = new ArrayList<ClientFriend>();
		synchronized (this.friends) {
			for (ClientFriend friend : this.friends) {
				if (name == null && friend.getName() == null) {
					result.add(friend);
				} else if (name != null && name.equalsIgnoreCase(friend.getName())) {
					result.add(friend);
				}
			}
		}
		return result;
	}

	@Override
	public List<ClientFriend> searchFriend(String partial) {
		if (partial == null) {
			throw new IllegalArgumentException("Cannot search for null");
		}
		String partialLowered = partial.toLowerCase(Locale.US);
		ArrayList<ClientFriend> result = new ArrayList<ClientFriend>();
		synchronized (this.friends) {
			for (ClientFriend friend : this.friends) {
				String name = (friend.getName() == null) ? null : friend.getName()
						.toLowerCase(Locale.US);
				if (name.contains(partialLowered)) {
					result.add(friend);
				}
			}
		}
		return result;
	}

	@Override
	public List<ClientFriend> getByStatus(ToxUserStatus status) {
		ArrayList<ClientFriend> result = new ArrayList<com.oneme.toplay.base.ClientFriend>();
		synchronized (this.friends) {
			for (com.oneme.toplay.base.ClientFriend friend : this.friends) {
				if (friend.isOnline() && friend.getStatus() == status) {
					result.add(friend);
				}
			}
		}
		return result;
	}

	@Override
	public List<com.oneme.toplay.base.ClientFriend> getOnlineFriends() {
		ArrayList<ClientFriend> result = new ArrayList<com.oneme.toplay.base.ClientFriend>();
		synchronized (this.friends) {
			for (ClientFriend friend : this.friends) {
				if (friend.isOnline()) {
					result.add(friend);
				}
			}
		}
		return result;
	}

	@Override
	public List<ClientFriend> getOfflineFriends() {
		ArrayList<ClientFriend> result = new ArrayList<ClientFriend>();
		synchronized (this.friends) {
			for (ClientFriend friend : this.friends) {
				if (!friend.isOnline()) {
					result.add(friend);
				}
			}
		}
		return result;
	}

	@Override
	public List<ClientFriend> all() {
		return new ArrayList<ClientFriend>(this.friends);
	}

	@Override
	public com.oneme.toplay.base.ClientFriend addFriend(int friendnumber) throws FriendExistsException {
        com.oneme.toplay.base.ClientFriend friend;
		synchronized (this.friends) {
			for (com.oneme.toplay.base.ClientFriend mfriend : this.friends) {
				if (mfriend.getFriendnumber() == friendnumber) {
					throw new FriendExistsException(mfriend.getFriendnumber());
				}
			}
			friend = new com.oneme.toplay.base.ClientFriend(friendnumber);
			this.friends.add(friend);
		}
		return friend;
	}

	@Override
	public ClientFriend addFriendIfNotExists(int friendnumber) {
		synchronized (this.friends) {
			for (ClientFriend mfriend : this.friends) {
				if (mfriend.getFriendnumber() == friendnumber) {
					return mfriend;
				}
			}
            com.oneme.toplay.base.ClientFriend friend = new ClientFriend(friendnumber);
			this.friends.add(friend);
			return friend;
		}
	}

	@Override
	public void removeFriend(int friendnumber) {
		synchronized (this.friends) {
			Iterator<ClientFriend> it = this.friends.iterator();
			while (it.hasNext()) {
				ToxFriend friend = it.next();
				if (friend.getFriendnumber() == friendnumber) {
					it.remove();
					break;
				}
			}
		}
	}

    */

}
