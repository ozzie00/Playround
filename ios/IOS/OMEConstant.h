//
//  OMEConstant.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/23/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#ifndef ToPlay_OMEConstant_h
#define ToPlay_OMEConstant_h


static NSString * const kOMEVersion = @"ToPlay 1.0.4";

static NSUInteger const kOMEInviteOtherMaximumCharacterCount = 140;

static double const kOMEFeetToMeters                     = 0.3048; // this is an exact value.
static double const kOMEFeetToMiles                      = 5280.0; // this is an exact value.
static double const kOMEInviteMaximumSearchDistance      = 100.0;
static double const kOMEMetersInAKilometer               = 1000.0; // this is an exact value.

static NSUInteger const kOMEInviteSearchLimit            = 50; // query limit for pins and tableviewcells
static NSUInteger const kOMEInviteRequirementSearchLimit = 100; 


// default value
static NSString * const kOMEdefaultsFilterDistanceKey = @"filterDistance";
static NSString * const kOMEdefaultsLocationKey       = @"currentLocation";
static double const kOMEdefaultsSearchRadius          = 1641; //meter

// default value for message cell
static CGFloat const kOMEdefaultMessageCellHeight     = 70.0;

// Parse API key constants:
static NSString * const kOMEParseClassKey     = @"ToPlayInvite";
static NSString * const kOMEParseUserKey      = @"user";
static NSString * const kOMEParseUsernameKey  = @"username";
static NSString * const kOMEParseUserLevelKey = @"userLevel";
static NSString * const kOMEParseUserIconKey  = @"userIcon";



static NSString * const kOMEParseTextKey                 = @"text";
static NSString * const kOMEParseLocationKey             = @"location";
static NSString * const kOMEParseWantToKey               = @"want to";
static NSString * const kOMEParseInvitePlaySubmitTimeKey = @"invitePlaySubmitTime";
static NSString * const kOMEParseReplyTimeKey            = @"replyTime";

// Parse API key corresponding to invite requirment
static NSString * const kOMEParseInviteSportTypeKey    = @"inviteSportType";
static NSString * const kOMEParseInvitePlayerNumberKey = @"invitePlayerNumber";
static NSString * const kOMEParseInvitePlayerLevelKey  = @"invitePlayerLevel";
static NSString * const kOMEParseInviteTimeKey         = @"inviteTime";
static NSString * const kOMEParseInviteCourtKey        = @"inviteCourt";
static NSString * const kOMEParseInviteFeeKey          = @"inviteFee";
static NSString * const kOMEParseInviteOtherInfoKey    = @"inviteOther";
static NSString * const kOMEParseInviteFromUserKey     = @"inviteFromUser";
static NSString * const kOMEParseInviteFromUsernameKey = @"inviteFromUsername";
static NSString * const kOMEParseInviteSubmitTimeKey   = @"inviteSubmitTime";

// Parse API key corresponding to message
static NSString * const kOMEParseMessageClassKey        = @"ToPlayMessage";
static NSString * const kOMEParseMessageFromUserKey     = @"messageFromUser";
static NSString * const kOMEParseMessageFromUsernameKey = @"messageFromUsername";
static NSString * const kOMEParseMessageToUserKey       = @"messageToUser";
static NSString * const kOMEParseMessageToUsernameKey   = @"messageToUsername";
static NSString * const kOMEParseMessageFromGroupKey    = @"messageFromGroup";
static NSString * const kOMEParseMessageToGroupKey      = @"messageToGroup";
static NSString * const kOMEParseMessageContentKey      = @"messageContent";
static NSString * const kOMEParseMessageSendTimeKey     = @"messageSendTime";
static NSString * const kOMEParseMessageAvatrImageKey   = @"messageAvatar";
static NSString * const kOMEParseMessageGroupTagKey     = @"messageGroupTag";
static NSString * const kOMEParseMessageKey             = @"messageKey";

// NSNotification userInfo keys:
static NSString * const kOMEFilterDistanceKey = @"filterDistance";
static NSString * const kOMELocationKey       = @"location";

// Notification names:
static NSString * const kOMEFilterDistanceChangeNotification = @"kOMEFilterDistanceChangeNotification";
static NSString * const kOMELocationChangeNotification       = @"kOMELocationChangeNotification";
static NSString * const kOMEInviteCreatedNotification        = @"kOMEInviteCreatedNotification";
static NSString * const kOMEMessageSendNotification          = @"kOMEMessageSendNotification";
static NSString * const kOMEMessageHaveNewNotification       = @"kOMEMessageListHaveNewNotification";
static NSString * const kOMEInternetReachableNotification    = @"kOMEInternetReachableNotification";


// UI strings:
static NSString * const kOMECannotViewInvite = @"change distance!";

#define OMELocationAccuracy double


// Define common constant string
static NSString * const kOMEParseLogout             = @"Log out";
static NSString * const kOMEParseLogin              = @"Logging in";
static NSString * const kOMEParseCancel             = @"Cancel";
static NSString * const kOMEParseError              = @"Error";
static NSString * const kOMEParseOK                 = @"OK";
static NSString * const kOMEParseNotLogin           = @"you did not login.";
static NSString * const kOMEParseNoUsername         = @"No username";
static NSString * const kOMEParseYes                = @"Yes";
static NSString * const kOMEParseNo                 = @"No";
static NSString * const kOMEParseNullString         = @"";
static NSString * const kOMEParseAnd                = @" and ";
static NSString * const kOMEParsePlease             = @"Please ";
static NSString * const kOMEParesEnterUsername      = @"enter username";
static NSString * const kOMEParseEnterPassword      = @"enter password";
static NSString * const kOMEParseEnterPasswordAgain = @"enter the same password again";
static NSString * const kOMEParseRefresh            = @"Loading";
static NSString * const kOMEParseInternetUnreachable= @"No internet connection";

static NSString * const kOMEParseNaviBackButton          = @"back";

static NSString * const kOMEParseNaviBackUppercaseButton = @"Back";

// Define constant string for App Delegate
static NSString * const kOMEParseInternetReachableWebSite = @"www.parse.com";

// Define constant string for login view
static NSString * const kOMEParseLoginUsernameError       = @"No this user";
static NSString * const kOMEParseLoginUsernameLengthError = @"Username length should not less 6 letters";
static NSString * const kOMEParseLoginPasswordError       = @"Password does not match";
static NSString * const kOMEParseLoginPasswordLengthError = @"Password length should not less 8 letters";
static NSString * const kOMEParseLoginNewUserCreateInfo   = @"Sign Up new user for you!";
static NSString * const kOMEParseLoginNewUserError        = @"Sign Up new user error!";


// Define constant string for invite view
static NSString * const kOMEParseInviteAlertInfo              = @"really invite someone to play?";
static NSString * const kOMEParseInviteLoginAlert             = @"please login or signup!";
static NSString * const kOMEParseInviteTitlePlay              = @"play";
static NSString * const kOMEParseInviteTitleIn                = @"in";
static NSString * const kOMEParseInviteReallyPlay             = @"Do you really invite someone to play?";
static NSString * const kOMEParseInviteSportTypeDefault       = @"Tennis";
static NSString * const kOMEParseInviteSportTypeDetailDefault = @"Detail";
static NSString * const kOMEParseInviteOtherDefault           = @"Good buddy!";
static NSString * const kOMEParseInviteCourtDefault           = @"MIT court";
static NSString * const kOMEParseInviteFeeDefault             = @"AA";
static NSString * const kOMEParseInviteFeeFree                = @"Free";
static NSString * const kOMEParseInviteErrorInfo              = @"invite player table view error";
static NSString * const kOMEParseInvitePlayerSubmitSuccessAlertInfo = @"Successfully submit invitation, please wait someone for join play!";
static NSString * const kOMEParseInviteSportTypeBadminton     = @"Badminton";
static NSString * const kOMEParseInviteSportTypeBasketball    = @"Basketball";
static NSString * const kOMEParseInviteSportTypeSnooker       = @"Snooker";
static NSString * const kOMEParseInviteSportTypeTableFootball = @"Table football";
static NSString * const kOMEParseInviteSportTypeTabeTennis    = @"Table Tennis";
static NSString * const kOMEParseInviteSportTypeTennis        = @"Tennis";
static NSString * const kOMEParseInviteSportTypeRunning       = @"Running";
static NSString * const kOMEParseInviteSportTypeCycling       = @"Cycling";



// Define constant string for invite requirement
static NSString * const kOMEParseInvitePlayerAlertInfo         = @"Do you really join this play?";
static NSString * const kOMEParseInvitePlayerInfoHeader        = @"Player Info";
static NSString * const kOMEParseInvitePlayerInfoPlayerName    = @"Name";
static NSString * const kOMEParseInvitePlayerInfoPlayerLevel   = @"Level";
static NSString * const kOMEParseInviteRequirementHeader       = @"Invite Requirement";
static NSString * const kOMEParseInviteRequirementSportType    = @"Sport";
static NSString * const kOMEParseInviteRequirementPlayerNumber = @"Number";
static NSString * const kOMEParseInviteRequirementPlayerLevel  = @"Level";
static NSString * const kOMEParseInviteRequirementTime         = @"Time";
static NSString * const kOMEParseInviteRequirementCourt        = @"Court";
static NSString * const kOMEParseInviteRequirementFee          = @"Fee";
static NSString * const kOMEParseInviteRequirementOther        = @"Other";
static NSString * const kOMEParseInviteJoinPlayHeader          = @"";
static NSString * const kOMEParseInviteJoinPlayButton          = @"Join Play";
static NSString * const kOMEParseInvitePlayerJoinPlaySuccessAlertInfo = @"Successfully submit request, please wait for reply!";

static NSString * const kOMEParseMessageJoinPlayRequestFirstPart  = @"Hi, ";
static NSString * const kOMEParseMessageJoinPlayRequestSecnodPart = @" want to join play";
static NSString * const kOMEParseMessageJoinPlayRequestThirdPart  = @" with you";
static NSString * const kOMEParseMessageJoinPlayNotSelf           = @"can not re-join yourself play!";



// Define constant string for Facebook
static NSString * const kOMEParseFacebookPublicProfile = @"public_profile";
static NSString * const kOMEParseFacebookEmail         = @"email";
static NSString * const kOMEParseFacebookUserFriends   = @"user_friends";


// Define constant string for setting view
static NSString * const kOMEParseSettingLogoutAlertInfo= @"this will not delete any data, You can still log in with this account.";
static NSString * const kOMEParseMe       = @"Me";
static NSString * const kOMEParseFeedback = @"Feedback";


// Define constant string for login view
static NSString * const kOMEParseLoginAlertInfo = @"can not log in:\n The username or password is wrong.";

// Define constant string for new user view
static NSString * const kOMEParseSignUp         = @"Signing Up";

// Define constant variable for storyboard
static NSInteger const kOMEMessageCellTag            = 101;
static NSInteger const kOMEMessageCellAvatarImageTag = 102;
static NSInteger const kOMEMessageCellNameTag        = 103;
static NSInteger const kOMEMessageCellContentTag     = 104;
static NSInteger const kOMEMessageCellTimeTag        = 105;

// Define constant variable for chat view
static NSString * const kOMEChatViewTitle             = @"Local Invitation";

// Define constant variable for message view
static NSUInteger const kOMEMessageListLimit          = 100;
static NSUInteger const kOMEMessageSearchLimit        = 100000;
static NSString * const kOMEMessageTitle              = @"Messages";
static NSString * const kOMEMessageSendButtonTitle    = @"Send";
static NSString * const kOMEMessageInputTextPlacehold = @"Reply message";
static NSString * const kOMEMessageNoUserInfo         = @"This username is null, no message";
static NSString * const kOMEMessageListNoForUser      = @"No message to you now";


// Define constant variable for cell
static NSString * const kOMEMessageCell = @"OMEMessageCell";
static NSString * const kOMEMemeCell    = @"OMEMemeCell";

// Define constant variable for feedback view
static NSString * const kOMEFeedbackTitle       = @"Feedback";
static NSString * const kOMEFeedbackReceiveInfo = @"ToPlay Team has received your feedback,Thanks again!";
static NSString * const kOMEFeedbackPlacehlod   = @"Thank you for your feedback and suggestion, ToPlay team will follow your feedback and suggestion to improve ToPlay app perfomance!\n\n                   ToPlay Team";





#endif
