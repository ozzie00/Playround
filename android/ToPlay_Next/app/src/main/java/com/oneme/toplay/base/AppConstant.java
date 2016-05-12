/*
* Copyright 2014 OneME
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/



package com.oneme.toplay.base;


public final class AppConstant {

    private static final String TAG = "AppConstant";

    public static final String OMETOPLAYPACKAGENAME        = "com.oneme.toplay";
    public static final String OMETOPLAYPARSEAPPLICATIONID = "E7Vla8ducchhXCBxbjTi1GfiqKJsHKZKa1TdGIke";
    public static final String OMETOPLAYPARSECLIENTKEY     = "B5cNapwaiststrI47QfZlKAxKBUxQggKOu7wfMtW";
    public static final String OMETOPLAYFACEBOOKAPPID      = "846009488767381";
    public static final String OMETOPLAYGOOGLEPLACEKEY     = "AIzaSyAWF807dWOPwod6y9-GIlnQgeARbA2BKug";//"AIzaSyDUyn7GO-5sI0h8Q507DCBiIGgzRUNi0NY";//"AIzaSyDoh6RGtWqEro0UtA2BqSur-GESBuFngss";

    // Note:  web server key, not mobile client key
    public static final String OMETOPLAYBAIDUPLACEKEY      = "7E72D397164211b08faadefec04b612a";

    public static final int OMEINVITEOTHERMAXIMUMCHARACTERCOUNT = 140;

    public static final double OMEFEETTOMETERS                = 0.3048; // this is an exact value.
    public static final double OMEFEETTOMILES                 = 5280.0; // this is an exact value.
    public static final double OMEINVITEMAXIMUMSEARCHDISTANCE = 100.0;
    public static final double OMEMETERSINAKILOMETER          = 1000.0; // this is an exact value.
    public static final int OMEINVITESEARCHLIMIT              = 50; // query limit for pins and tableviewcells
    public static final int OMEINVITEREQUIREMENTSEARCHLIMIT   = 100;
    public static final int OMEPHONEMINIMUMLENGTH             = 3;
    public static final String OMEPARSESPORTVALUENULL         = "0";
    public static final int OMEPARSEUSERICONRADIUS            = 30;
    public static final int OMEPARSEBUFFERLENGTH              = 1024;
    public static final Double OMEPARSEMAXLATITUDE            = 90.0D;
    public static final Double OMEPARSEMINLATITUDE            = -90.0D;
    public static final Double OMEPARSEMAXLONGITUDE           = 180.0D;
    public static final Double OMEPARSEMINLONGITUDE           = -180.0D;




    // define special symbol
    public static final String OMEPARSEHYPHENSTRING           = "-";
    public static final String OMEPARSESLASHSTRING            = "/";
    public static final String OMEPARSESPACESTRING            = " ";
    public static final String OMEPARSECOLONSTRING            = ":";
    public static final String OMEPARSECOLONZEROSTRING        = ":0";
    public static final String OMEPARSEDISTANCEFORMATSTRING   = "##.00";
    public static final String OMEPARSEZEROSTRING             = "0";
    public static final String OMEPARSECOMMASTRING            = ",";
    public static final String OMEPARSEPLUSSTRING             = "+";

    // define Image format
    public static final String OMETOPLAYIMAGEPNGFORMAT        = ".png";







    // default color value
    public static final int OMETOPLAYDEFAULTCOLOR             = 0xFFF3B64D;
    public static final String OMETOPLAYOLYMPICBLUE           = "247291";
    public static final String OMETOPLAYOLYMPICGREEN          = "1DA155";
    public static final String OMETOPLAYOLYMPICRED            = "DF354A";
    public static final String OMETOPLAYOLYMPICYELLOW         = "F3B64D";
    public static final String OMETOPLAYGRAY                  = "B6B2A9";

    // default value
    public static final String OMEDEFAULTFILTERDISTSTANCEKEY  = "filterDistance";
    public static final String OMEDEFAULTLOCATIONKEY          = "currentLocation";
    public static final double OMEDEFAULTSEARCHRADIUS         = 1641; //meter

    // default value for message cell
    public static final double OMEDEFAULTMESSAGECELLHEIGHT    = 70.0;

    // Parse API key constants for class
    public static final String OMETOPLAYINVITECLASSKEY         = "ToPlayInvite";
    public static final String OMETOPLAYMESSAGECLASSKEY        = "ToPlayMessage";
    public static final String OMETOPLAYGROUPCLASSKEY          = "PlayroundGroup";
    public static final String OMETOPLAYPLAYERCLASSKEY         = "ToPlayPlayer";
    public static final String OMETOPLAYVENUECLASSKEY          = "ToPlayVenue";
    public static final String OMETOPLAYVENUEOWNERCLASSKEY     = "PlayroundVenueOwner";
    public static final String OMETOPLAYINVITECOMMENTCLASSKEY  = "PlayroundInviteComment";
    public static final String OMETOPLAYINVITELIKECLASSKEY     = "PlayroundInviteLike";
    public static final String OMETOPLAYINVITESCORECLASSKEY    = "PlayroundInviteScore";
    public static final String OMETOPLAYFOLLOWINGCLASSKEY      = "PlayroundFollowing";
    public static final String OMETOPLAYVENUECOMMENTCLASSKEY   = "PlayroundVenueComment";
    public static final String OMETOPLAYVENUELIKECLASSKEY      = "PlayroundVenueLike";
    public static final String OMETOPLAYVENUEASHOMECLASSKEY    = "PlayroundVenueAsHome";
    public static final String OMETOPLAYBOOKINGVENUECLASSKEY   = "PlayroundBookingVenue";
    public static final String OMETOPLAYPHOTOCLASSKEY          = "PlayroundPhoto";
    public static final String OMETOPLAYPHOTOLINKCLASSKEY      = "PlayroundPhotoLink";
    public static final String OMETOPLAYPAYPRIMECLASSKEY       = "PlayroundPayPrime";
    public static final String OMETOPLAYTHIRDAPIREQUESTCLASSKEY= "PlayroundThirdRequest";

    // Parse API key constants related to user :
    public static final String OMEPARSEUSERKEY             = "user";
    public static final String OMEPARSEUSERSKEY            = "users";
    public static final String OMEPARSEUSEROMEIDKEY        = "omeID";
    public static final String OMEPARSEUSERNAMEKEY         = "username";
    public static final String OMEPARSEUSERLEVELKEY        = "userLevel";
    public static final String OMEPARSEUSERICONKEY         = "userIcon";
    public static final String OMEPARSECREATEDATKEY        = "createdAt";
    public static final String OMEPARSEUSERPASSWORDKEY     = "password";
    public static final String OMEPARSEUSERALIASKEY        = "userAlias";
    public static final String OMEPARSEUSERICONFILENAME    = "userIcon.png";
    public static final String OMEPARSEUSEROMEIDNULL       = "0";
    public static final int OMEPARSEAVATARFILETYPEPNG      = 1;
    public static final String OMEPARSEUSERHOMEVENUEKEY    = "userHomeVenue";
    public static final String OMEPARSEUSERBACKUPVENUEKEY  = "userBackupVenue";
    public static final String OMEPARSEUSERHOMESPORTKEY    = "userHomeSport";
    public static final String OMEPARSEUSERBACKUPSPORTKEY  = "userBackupSport";
    public static final String OMEPARSEUSERHOMEVENUEPHONEKEY    = "userHomeVenuePhone";
    public static final String OMEPARSEUSERBACKUPVENUEPHONEKEY  = "userBackupVenuePhone";

    public static final String OMEPARSEUSERPHONEKEY          = "userPhone";
    public static final String OMEPARSEUSERPAYMENTMETHODKEY  = "userPayment";
    public static final String OMEPARSEUSERPAYMENTACCOUNTKEY = "userPaymentAccount";

    // Parse API key corresponding to user payment method enum
    public static final String OMEPARSEUSERPAYMENTMETHODALIPAY = "ALIPAY";
    public static final String OMEPARSEUSERPAYMENTMETHODPAYPAL = "PAYPAL";
    public static final String OMEPARSEUSERPAYMENTMETHODCREDIT = "CREDIT";


    // Parse API key constants for user
    public static final String OMEPARSEUSERPROFILENAMEKEY  = "userprofilename";
    public static final int OMEPARSEUSERPROFILENAMESUFFIXLENGTH = 6;
    public static final String OMEPARSEUSERPROFILEAVATARKEY  = "userprofileavatar";
    public static final int OMEPARSEUSERNAMEMINIMUMLENGTH     = 6;
    public static final int OMEPARSEUSERPASSWORDMINIMUMLENGTH = 6;
    public static final int OMEPARSEUSERAVATARCOMPRESSQUILTY  = 90;

    public static final String OMEPARSEUSERLASTTIMEKEY     = "userlasttime";
    public static final String OMEPARSEUSERLASTLOCATIONKEY = "userlastlocation";
    public static final String OMEPARSEUSERQRCODEKEY       = "userqrcode";
    public static final String OMEPARSEUSERTAGKEY          = "userTag";
    public static final String OMEPARSEUSERTAGPLAYER       = "player";
    public static final String OMEPARSEUSERTAGVENUE        = "venue";
    public static final String OMEPARSEUSEROTHERKEY        = "userother";
    public static final String OMEPARSEUSERDEVICEIDKEY     = "userDeviceID";

    // Parse API key constants for user preference
    public static final String OMEPARSEUSERACTIVEACCOUNTSKEY  = "active_account";
    public static final String OMEPARSEUSERNICKNAMEKEY     = "nickname";
    public static final String OMEPARSEUSERSTATUSKEY       = "status";
    public static final String OMEPARSEUSERSTATUALERTSKEY  = "status_message";
    public static final String OMEPARSEUSERIDKEY           = "tox_id";
    public static final String OMEPARSEUSERLOGGEDINSKEY    = "loggedin";
    public static final String OMEPARSEUSERIDHEAER         = "";//"tox:";
    public static final String OMEPARSEADDFRIENDACTION     = "action";
    public static final String OMEPARSELANGUAGEKEY         = "language";
    public static final String OMEPARSEWIFIONLYKEY         = "wifi_only";
    public static final String OMEPARSENOSPAMKEY           = "nospam";
    public static final String OMEPARSELOGOUTKEY           = "logout";

    // Parse API key constants for player
  //  public static final String OMEPARSEUSERPROFILENAMEKEY  = "userprofilename";
  //  public static final int OMEPARSEUSERPROFILENAMESUFFIXLENGTH = 6;
    public static final String OMEPARSEPLAYERRATEKEY       = "playerrating";
    public static final String OMEPARSEPLAYERAVATARKEY     = "playeravatar";

    // Parse API Constant for venue
    public static final String OMETOPLAYVENUENAMEKEY         = "venueName";
    public static final String OMETOPLAYVENUEICONKEY         = "venueIcon";
    public static final String OMETOPLAYVENUELEVELKEY        = "venueLevel";
    public static final String OMETOPLAYVENUETYPEKEY         = "venueType";
    public static final String OMETOPLAYVENUEADDRESSKEY      = "venueAddress";
    public static final String OMETOPLAYVENUELOCATIONKEY     = "venueLocation";
    public static final String OMETOPLAYVENUEPHONEKEY        = "venuePhone";
    public static final String OMETOPLAYVENUECOURTNUMBERKEY  = "venueCourtNumber";
    public static final String OMETOPLAYVENUELIGHTEDKEY      = "venueLighted";
    public static final String OMETOPLAYVENUEINDOORKEY       = "venueIndoor";
    public static final String OMETOPLAYVENUEPUBLICKEY       = "venuePublic";
    public static final String OMETOPLAYVENUEUPLOADEDBYKEY   = "venueUploadedby";
    public static final String OMETOPLAYVENUEPLAYERASHOMEKEY = "venuePlayerAsHome";
    public static final String OMETOPLAYVENUESEARCHKEY       = "venueSearch";
    public static final String OMETOPLAYVENUEDESCRIPTIONKEY  = "venueDescriprion";
    public static final String OMETOPLAYVENUEBUSINESSKEY     = "venueBusiness";
    public static final String OMETOPLAYVENUEPRICEKEY        = "venuePrice";
    public static final String OMETOPLAYVENUEPRIMEINFOKEY    = "venuePrimeInfo";

    // Parse API key constants related to venue business
    public static final String OMETOPLAYVENUEBUSINESSPRIME   = "venuePrime";
    public static final String OMETOPLAYVENUEBUSSINESSFREE   = "venueFree";

    // Parse API key constants related to venue prime membership json format
    public static final String OMETOPLAYVENUEJSON3RD         = "3rd";
    public static final String OMETOPLAYVENUEJSONNAMEID      = "name id";
    public static final String OMETOPLAYVENUEJSONNAME        = "name";
    public static final String OMETOPLAYVENUEJSONLIST        = "list";
    public static final String OMETOPLAYVENUEJSONCURRENCY    = "currency";
    public static final String OMETOPLAYVENUEJSONCARDID      = "card id";
    public static final String OMETOPLAYVENUEJSONCARDNAME    = "card name";
    public static final String OMETOPLAYVENUEJSONCARDPRICE   = "card price";


    // Parse API key constants related to venue owner :
    public static final String OMEPARSEVENUEOWNERNAMEKEY         = "ownerName";
    public static final String OMEPARSEVENUEOWNEROMEIDKEY        = "ownerOmeID";
    public static final String OMEPARSEVENUEOWNERICONKEY         = "ownerIcon";
    public static final String OMEPARSEVENUEOWNERALIASKEY        = "ownerAlias";
    public static final String OMEPARSEVENUEOWNERICONFILENAME    = "ownerIcon.png";
    public static final String OMEPARSEVENUEOWNEROMEIDNULL       = "0";
    public static final String OMEPARSEVENUEOWNERPHONEKEY        = "ownerPhone";
    public static final String OMEPARSEVENUEOWNERADDRESSKEY      = "ownerAddress";
    public static final String OMEPARSEVENUEOWNERLOCATIONKEY     = "ownerLocation";
    public static final String OMEPARSEVENUEOWNERCOURTNUMBERKEY  = "ownerCourtNumber";
    public static final String OMEPARSEVENUEOWNERLIGHTEDKEY      = "ownerLighted";
    public static final String OMEPARSEVENUEOWNERINDOORKEY       = "ownerIndoor";
    public static final String OMEPARSEVENUEOWNERPUBLICKEY       = "ownerPublic";
    public static final String OMEPARSEVENUEOWNERVERIFYKEY       = "ownerVerify";
    public static final String OMEPARSEVENUEOWNERCONTACTNAMEKEY  = "contactname";
    public static final String OMEPARSEVENUEOWNERCONTACTPHONEKEY = "contactphone";
    public static final String OMEPARSEVENUEOWNERCONTACTEMAILKEY = "contactemail";
    public static final String OMEPARSEVENUEOWNERIDCOPYKEY       = "ownerIdCopy";
    public static final String OMEPARSEVENUEOWNERLICENSECOPYKEY  = "ownerLicense";
    public static final String OMEPARSEVENUEOWNERIDCOPYFILENAME  = "ownerId.png";
    public static final String OMEPARSEVENUEOWNERLICENSEFILENAME = "ownerLicense.png";

    // Parse API key constants related to photo
    public static final String OMETOPLAYPHOTOPICTUREKEY     = "photoFile";
    public static final String OMETOPLAYPHOTOTHUMBNAILKEY   = "photoThumb";
    public static final String OMETOPLAYPHOTOUPLOADEDBYKEY  = "photoUploadedby";

    // Parse API key constants related to photo link
    public static final String OMETOPLAYPHOTOLINKOBJECTKEY         = "photoLinkObject";
    public static final String OMETOPLAYPHOTOLINKPHOTOTOBJECTKEY   = "photoLinkPhoto";
    public static final String OMETOPLAYPHOTOLINKTYPEKEY           = "photoLinkType";

    // Parse API key constants related to photo link type
    public static final String OMETOPLAYPHOTOTYPEVENUE   = "venuePhoto";
    public static final String OMETOPLAYPHOTOTYPEUSER    = "userPhoto";

    // Invoke activity return result
    public static final int OMEPARSEUSERVENUEASHOMERESULT           = 0;
    public static final int OMEPARSEUSERVENUEASBACKUPRESULT         = 1;
    public static final int OMEPARSEVENUOWNERIDCOPYIMAGERESULT      = 2;
    public static final int OMEPARSEVENUOWNERIDCOPYPHOTORESULT      = 3;
    public static final int OMEPARSEVENUOWNERLICENSECOPYIMAGERESULT = 4;
    public static final int OMEPARSEVENUOWNERLICENSECOPYPHOTORESULT = 5;
    public static final int OMEPARSEINVITESEARCHLOCATIONRESULT      = 6;
    public static final int OMEPARSEBOOKINGVENUEPAYRESULT           = 7;
    public static final int OMEPARSEBUYPRIMEMEMBERSHIPPAYRESULT     = 8;
    public static final int OMEPARSEUPLOADVENUESEARCHLOCATIONRESULT = 9;

    // Parse API Constant corresponding to upload venue key
    public static final String OMEPARSEUPLOADVENUEKEY            = "uploadByUser";


    // Parse API Constant corresponding to add friend

    public static final int OMETOPLAYUSERQRCODESIZE        = 600;
    public static final int OMETOPLAYUSERPUBLICKEYLENGTH   = 76;
    public static final int OMETOPLAYNOVALIDPUBLICKEY      = -1;
    public static final int OMETOPLAYFRIENDEXIST           = -2;
    public static final int OMETOPLAYNOKEYOWN              = -3;
    public static final String OMETOPLAYUSERQRCODEFILE     = "/toplay/playround_user_qr.png";
    public static final String OMETOPLAYUSERQRCODEPATH     = "/toplay/";
    public static final String OMETOPLAYUSERQRCODEFILETYPE = "image/jpeg";
    public static final String OMETOPLAYUSERQRCODNOMEDIA   = ".nomedia";
    public static final String OMETOPLAYUSERQRCODEFILENAME = "playround_user_qr.png";//"userkey_qr.png";


    // Parse API Constant corresponding to chat
    public static final String OMETOPLAYATTACHMENTFILENAME = "toplayavatar";//"userkey_qr.png";
    public static final String OMETOPLAYAVATARTEMPFILENAME = "toplayavatartmp";



    // Parse API key corresponding to invite
    public static final String OMEPARSETEXTKEY                 = "text";
    public static final String OMEPARSELOCATIONKEY             = "location";
    public static final String OMEPARSEWANTTOKEY               = "want to";
    public static final String OMEPARSEINVITEPLAYSUBMITTIMEKEY = "invitePlaySubmitTime";
    public static final String OMEPARSEREPLYTIMEKEY            = "replyTime";
    public static final String OMEPARSEINVITEDEFAULTSPORTVALUE = "Walking";

    // Parse API key corresponding to invite requirment
    public static final String OMEPARSEINVITESPORTTYPEKEY        = "inviteSportType";
    public static final String OMEPARSEINVITESPORTTYPEVALUEKEY   = "inviteSportTypeValue";
    public static final String OMEPARSEINVITEPLAYERNUMBERKEY     = "invitePlayerNumber";
    public static final String OMEPARSEINVITEPLAYERLEVELKEY      = "invitePlayerLevel";
    public static final String OMEPARSEINVITETIMEKEY             = "inviteTime";
    public static final String OMEPARSEINVITECOURTKEY            = "inviteCourt";
    public static final String OMEPARSEINVITEFEEKEY              = "inviteFee";
    public static final String OMEPARSEINVITEOTHERINFOKEY        = "inviteOther";
    public static final String OMEPARSEINVITEFROMUSERKEY         = "inviteFromUser";
    public static final String OMEPARSEINVITEFROMUSERNAMEKEY     = "inviteFromUsername";
    public static final String OMEPARSEINVITESUBMITTIMEKEY       = "inviteSubmitTime";
    public static final String OMEPARSEINVITEJOINPLAYOBJECTIDKEY = "inviteObjectID";
    public static final String OMEPARSEINVITEPUBLICKEY           = "invitePublic";
    public static final String OMEPARSESHARETEXTFILE             = "text/plain";
    public static final String OMEPARSEINVITEWORKNAMEKEY         = "inviteWorkoutName";

    public static final String OMEPARSEINVITEWORKOUTIMAGEFILEKEY = "inviteWorkoutImageFile";
    public static final String OMEPARSEINVITEWORKOUTIMAGEKEY    = "toplayworkoutimage";
    public static final String OMEPARSEINVITEWORKOUTFILENAME    = "inviteworkout.png";

    // Parse API key corresponding to message
    public static final String OMEPARSEMESSAGEFROMUSERKEY     = "messageFromUser";
    public static final String OMEPARSEMESSAGEFROMUSERNAMEKEY = "messageFromUsername";
    public static final String OMEPARSEMESSAGETOUSERKEY       = "messageToUser";
    public static final String OMEPARSEMESSAGETOUSERNAMEKEY   = "messageToUsername";
    public static final String OMEPARSEMESSAGEFROMGROUPKEY    = "messageFromGroup";
    public static final String OMEPARSEMESSAGETOGROUPKEY      = "messageToGroup";
    public static final String OMEPARSEMESSAGECONTENTKEY      = "messageContent";
    public static final String OMEPARSEMESSAGESENDTIMEKEY     = "messageSendTime";
    public static final String OMEPARSEMESSAGEAVATRIMAGEKEY   = "messageAvatar";
    public static final String OMEPARSEMESSAGEGROUPTAGKEY     = "messageGroupTag";
    public static final String OMEPARSEMESSAGEKEY             = "messageKey";
    public static final String OMEPARSEMESSAGEFOROBJECTIDKEY  = "messageForObjectIDKey";
    public static final String OMEPARSEMESSAGEFROMOMEIDKEY    = "messageFromOmeIDKey";

    // Parse API key corresponding to invite comment key
    public static final String OMEPARSEINVITECOMMENTAUTHORKEY     = "inviteCommentAuthor";
    public static final String OMEPARSEINVITECOMMENTAUTHORNAMEKEY = "inviteCommentAuthorName";
    public static final String OMEPARSEINVITECOMMENTCONTENTKEY    = "inviteCommentContent";
    public static final String OMEPARSEINVITECOMMENTPARENTIDKEY   = "inviteCommentParentId";
    public static final String OMEPARSEINVITECOMMENTPUBLICKEY     = "inviteCommentPublic";
    public static final String OMEPARSEINVITECOMMENTSUBMITTIMEKEY = "inviteCommentSubmitTime";

    // Parse API key corresponding to invite like key
    public static final String OMEPARSEINVITELIKEAUTHORKEY     = "inviteLikeAuthor";
    public static final String OMEPARSEINVITELIKEAUTHORNAMEKEY = "inviteLikeAuthorName";
    public static final String OMEPARSEINVITELIKEPARENTIDKEY   = "inviteLikeParentId";
    public static final String OMEPARSEINVITELIKETYPEKEY       = "inviteLikeType";
    public static final String OMEPARSEINVITELIKEOTHERKEY      = "inviteLikeOther";

    // Parse API key corresponding to invite score key
    public static final String OMEPARSEINVITESCOREAUTHORKEY       = "inviteScoreAuthor";
    public static final String OMEPARSEINVITESCOREAUTHORNAMEKEY   = "inviteScoreAuthorName";
    public static final String OMEPARSEINVITESCORECONTENTKEY      = "inviteScoreContent";
    public static final String OMEPARSEINVITESCORERATEKEY         = "inviteScoreRate";
    public static final String OMEPARSEINVITESCOREPARENTIDKEY     = "inviteScoreParentId";
    public static final String OMEPARSEINVITESCOREWORKOUTTYPEKEY  = "inviteScoreSportType";
    public static final String OMEPARSEINVITESCOREWORKOUTVALUEKEY = "inviteScoreSportValue";

    // Parse API key corresponding to group key
    public static final String OMEPARSEGROUPADMINKEY          = "GroupAdmin";
    public static final String OMEPARSEGROUPADMINNAMEKEY      = "GroupAdminUserName";
    public static final String OMEPARSEGROUPWORKOUTNAMEKEY    = "GroupWorkoutName";
    public static final String OMEPARSEGROUPPARENTIDKEY       = "GroupParentId";
    public static final String OMEPARSEGROUPPUBLICKEY         = "GroupPublic";
    public static final String OMEPARSEGROUPSPORTKEY          = "GroupSport";
    public static final String OMEPARSEGROUPSPORVALUETKEY     = "GroupSportValue";
    public static final String OMEPARSEGROUPICONKEY           = "GroupIcon";
    public static final String OMEPARSEGROUPMEMBERUSERKEY     = "MemberUser";
    public static final String OMEPARSEGROUPMEMBERUSERNAMEKEY = "MemberUserName";
    public static final String OMEPARSEGROUPMEMBERJOINTIMEKEY = "MemberJoinTime";

    // Parse API key corresponding to following player key
    public static final String OMEPARSEFOLLOWINGPLAYERUSERKEY     = "followingUser";
    public static final String OMEPARSEFOLLOWINGPLAYERUSERNAMEKEY = "followingUserName";
    public static final String OMEPARSEFOLLOWERPLAYERUSERKEY      = "followerUser";
    public static final String OMEPARSEFOLLOWERPLAYERUSERNAMEKEY  = "followerUsername";
    public static final String OMEPARSEFOLLOWINGPLAYEROTHERKEY    = "followingOther";

    // Parse API key corresponding to venue comment key
    public static final String OMEPARSEVENUECOMMENTAUTHORKEY     = "venueCommentAuthor";
    public static final String OMEPARSEVENUECOMMENTAUTHORNAMEKEY = "venueCommentAuthorName";
    public static final String OMEPARSEVENUECOMMENTCONTENTKEY    = "venueCommentContent";
    public static final String OMEPARSEVENUECOMMENTPARENTIDKEY   = "venueCommentParentId";
    public static final String OMEPARSEVENUECOMMENTPUBLICKEY     = "venueCommentPublic";
    public static final String OMEPARSEVENUECOMMENTSUBMITTIMEKEY = "venueCommentSubmitTime";

    // Parse API key corresponding to venue like key
    public static final String OMEPARSEVENUELIKEAUTHORKEY     = "venueLikeAuthor";
    public static final String OMEPARSEVENUELIKEAUTHORNAMEKEY = "venueLikeAuthorName";
    public static final String OMEPARSEVENUELIKEPARENTIDKEY   = "venueLikeParentId";
    public static final String OMEPARSEVENUELIKEOTHERKEY      = "venueLikeOther";

    // Parse API key corresponding to venue as home key
    public static final String OMEPARSEVENUEHOMEAUTHORKEY     = "venueAsHomeAuthor";
    public static final String OMEPARSEVENUEHOMEAUTHORNAMEKEY = "venueAsHomeAuthorName";
    public static final String OMEPARSEVENUEHOMEPARENTIDKEY   = "venueAsHomeParentId";
    public static final String OMEPARSEVENUEHOMEOTHERKEY      = "venueAsHomeOther";

    // Parse API key corresponding to venue access
    public static final String OMEPARSEVENUEACCESSPUBLIC      = "Public";
    public static final String OMEPARSEVENUEACCESSPRIVATE     = "Private";

    // Parse API key corresponding to booking venue key
    public static final String OMEPARSEBOOKINGAUTHORKEY        = "bookingAuthor";
    public static final String OMEPARSEBOOKINGAUTHORNAMEKEY    = "bookingAuthorName";
    public static final String OMEPARSEBOOKINGVENUEOBJECTIDKEY = "bookingVenueId";
    public static final String OMEPARSEBOOKINGVENUENAMEKEY     = "bookingVenueName";
    public static final String OMEPARSEBOOKINGTIMEKEY          = "bookingTime";
    public static final String OMEPARSEBOOKINGSUBMITTIMEKEY    = "bookingSubmitTime";
    public static final String OMEPARSEBOOKINGREMARKKEY        = "bookingRemark";
    public static final String OMEPARSEBOOKINGPAYSTATUSKEY     = "bookingPayStatus";
    public static final String OMEPARSEBOOKINGPAYNUMBERKEY     = "bookingPayNumber";
    public static final String OMEPARSEBOOKINGREFUNDSTATUSKEY  = "bookingRefundStatus";
    public static final String OMEPARSEBOOKINGFINISHSTATUSKEY  = "bookingFinishStatus";
    public static final String OMEPARSEBOOKINGOTHERKEY         = "bookingOther";

    // Parse API key corresponding to booking status enum
    public static final String OMEPARSEBOOKINGPAYSUCCESS       = "SUCCESS";
    public static final String OMEPARSEBOOKINGPAYFAIL          = "FAIL";
    public static final String OMEPARSEBOOKINGPAYINPROGRESS    = "INPROGRESS";
    public static final String OMEPARSEBOOKINGREFUNDSUCCESS    = "SUCCESS";
    public static final String OMEPARSEBOOKINGREFUNDFAIL       = "FAIL";
    public static final String OMEPARSEBOOKINGREFUNDINPROGRESS = "INPROGRESS";
    public static final String OMEPARSEBOOKINGREFUNDNOTSTART   = "NOTSTART";
    public static final String OMEPARSEBOOKINGFININSHED        = "FINISHED";
    public static final String OMEPARSEBOOKINGFINISHNOTSTART   = "NOTSTART";
    public static final String OMEPARSEBOOKINGFINISHCANCEL     = "CANCEL";
    public static final String OMEPARSEBOOKINGFINISHOTHER      = "OTHER";

    // Parse API key corresponding to prime membership key
    public static final String OMEPARSEPRIMEUSERNAMEKEY        = "primeUsername";
    public static final String OMEPARSEPRIMEUSERPHONEKEY       = "primeUserphone";
    public static final String OMEPARSEPRIME3RDKEY             = "prime3rd";
    public static final String OMEPARSEPRIMEVENUENAMEKEY       = "primeVenuename";
    public static final String OMEPARSEPRIMEVENUEIDKEY         = "primeVenueId";
    public static final String OMEPARSEPRIMECARDNAMEKEY        = "primeCardname";
    public static final String OMEPARSEPRIMECARDIDKEY          = "primeCardId";
    public static final String OMEPARSEPRIMECARDPRICEKEY       = "primeCardPrice";
    public static final String OMEPARSEPRIMEPAYTIMEKEY         = "primePayTime";
    public static final String OMEPARSEPRIMEPAYNUMBERKEY       = "primePayNumber";
    public static final String OMEPARSEPRIMESMSCODEKEY         = "primeSMSCode";
    public static final String OMEPARSEPRIMEPAYCURRENCYKEY     = "primePayCurrency";
    public static final String OMEPARSEPRIMEOTHERKEY           = "primeOther";

    // Parse API key corresponding to third request key
    public static final String OMEPARSETHIRDNAMEKEY            = "thirdName";
    public static final String OMEPARSETHIRDSERVICEKEY         = "thirdService";
    public static final String OMEPARSETHIRDREQUESTHTTPKEY     = "thirdRequestHttp";

    // Parse API key corresponding to third request service type
    public static final String OMEPARSETHIRDSERVICEPRIME       = "Prime";
    public static final String OMEPARSETHIRDSERVICEMAPPLACE    = "MapPlace";

    // Parse API key corresponding to third request http json format
    public static final String OMEPARSETHIRDHTTPJSONAPIBASE    = "api base";
    public static final String OMEPARSETHIRDHTTPJSONAPIMOBILE  = "api mobile";
    public static final String OMEPARSETHIRDHTTPJSONAPICARDID  = "api cardid";
    public static final String OMEPARSETHIRDHTTPJSONAPICARDNUMBER  = "api cardnumber";
    public static final String OMEPARSETHIRDHTTPJSONAPIPAYNUMBER   = "api paynumber";



    // Parse API default value for invite score
    public static final int OMEPARSEINVITESCOREZERO            = 0;
    public static final int OMEPARSEINVITESCOREPERFECT         = 100;

    // Parse API default value for booking venue
    public static final String OMEPARSEBOOKINGVENUEADVANCEDEPOSIT = "10.00";


    // NSNotification userInfo keys:
    public static final String OMEFILTERDISTANCEKEY = "filterDistance";
    public static final String OMELOCATIONKEY       = "location";

    // Notification names:
    public static final String OMEFILTERDISTANCECHANGENOTIFICATION = "OMEFilterDistanceChangeNotification";
    public static final String OMELOCATIONCHANGENOTIFICATION       = "OMELocationChangeNotification";
    public static final String OMEINVITECREATEDNOTIFICATION        = "OMEInviteCreatedNotification";
    public static final String OMEMESSAGESENDNOTIFICATION          = "OMEMessageSendNotification";
    public static final String OMEMESSAGEHAVENEWNOTIFICATION       = "OMEMessageListHaveNewNotification";
    public static final String OMEINTERNETREACHABLENOTIFICATION    = "OMEInternetReachableNotification";


    // UI strings:
    public static final String OMECANNOTVIEWINVITE = "change distance!";

   // #define OMELocationAccuracy double


   // Define common constant string
    public static final String OMEPARSELOGOUT             = "Logout";
    public static final String OMEPARSELOGIN              = "Login";
    public static final String OMEPARSESETTING            = "Setting";
    public static final String OMEPARSECANCEL             = "Cancel";
    public static final String OMEPARSEERROR              = "Error";
    public static final String OMEPARSEOK                 = "OK";
    public static final String OMEPARSENOTLOGIN           = "you did not login.";
    public static final String OMEPARSENOUSERNAME         = "No username";
    public static final String OMEPARSEYES                = "Yes";
    public static final String OMEPARSENO                 = "No";
    public static final String OMEPARSENULLSTRING         = "";
    public static final String OMEPARSEAND                = " and ";
    public static final String OMEPARSEPLEASE             = "Please ";
    public static final String OMEPARSEENTERUSERNAME      = "enter username";
    public static final String OMEPARSEENTERPASSWORD      = "enter password";
    public static final String OMEPARSEENTERPASSWORDAGAIN = "enter the same password again";
    public static final String OMEPARSEREFRESH            = "Loading";
    public static final String OMEPARSEINTERNETUNREACHABLE= "No internet connection";

    public static final String OMEPARSENAVIBACKBUTTON          = "back";

    public static final String OMEPARSENAVIBACKUPPERCASEBUTTON = "Back";

    // Define constant string for App Delegate
    public static final String OMEPARSEINTERNETREACHABLEWEBSITE = "www.parse.com";

    // Define constant string for login view
    public static final String OMEPARSELOGINUSERNAMEERROR       = "No this user";
    public static final String OMEPARSELOGINUSERNAMELENGTHERROR = "Username length should not less 6 letters";
    public static final String OMEPARSELOGINPASSWORDERROR       = "Password does not match";
    public static final String OMEPARSELOGINPASSWORDLENGTHERROR = "Password length should not less 8 letters";
    public static final String OMEPARSELOGINNEWUSERCREATEINFO   = "Sign Up new user for you!";
    public static final String OMEPARSELOGINNEWUSERERROR        = "Sign Up new user error!";


    // Define constant string for invite view
    public static final String OMEPARSEINVITEPLAYBUTTON             = "Invite Play";
    public static final String OMEPARSEINVITEACCESSRIGHTCHOOSE      = "Pick Type";
    public static final String OMEPARSEINVITESPORTTYPECHOOSE        = "Pick Type";
    public static final String OMEPARSEINVITEPLAYERLEVELCHOOSE      = "Pick Level";
    public static final String OMEPARSEINVITEPLAYERNUMBERCHOOSE     = "Pick Number";
    public static final String OMEPARSEINVITEPLAYFEECHOOSE          = "Pick Fee";
    public static final String OMEPARSEINVITEALERTINFO              = "really invite someone to play?";
    public static final String OMEPARSEINVITELOGINALERT             = "please login or signup!";
    public static final String OMEPARSEINVITETITLEPLAY              = "play";
    public static final String OMEPARSEINVITETITLEIN                = "in";
    public static final String OMEPARSEINVITEREALLYPLAY             = "Do you really invite someone to play?";
    public static final String OMEPARSEINVITESPORTTYPEDEFAULT       = "Tennis";
    public static final String OMEPARSEINVITESPORTTYPEDETAILDEFAULT = "Detail";
    public static final String OMEPARSEINVITEOTHERDEFAULT           = "Good buddy!";
    public static final String OMEPARSEINVITECOURTDEFAULT           = "MIT court";
    public static final String OMEPARSEINVITEPLAYERLEVELDEFAULT     = "1";
    public static final String OMEPARSEINVITEPLAYERNUMBERDEFAULT    = "1";
    public static final String OMEPARSEINVITEFEEDEFAULT             = "AA";
    public static final String OMEPARSEINVITEFEEFREE                = "Free";
    public static final String OMEPARSEINVITEERRORINFO              = "invite player table view error";
    public static final String OMEPARSEINVITEPLAYERSUBMITSUCCESSALERTINFO = "Successfully submit invitation, please wait someone for join play!";
    public static final int OMEPARSEINVITETIMETWOBITDIVIDER         = 10;
    public static final int OMEPARSEINVITEYEARSTART                 = 2014;
    public static final int OMEPARSEINVITEYEAREND                   = 2035;





    // Define constant string for invite sport type
    public static final String OMEPARSEINVITEACCESSRIGHTPUBLIC      = "Public";
    public static final String OMEPARSEINVITEACCESSRIGHTFRIEND      = "Friends";
    public static final String OMEPARSEINVITEACCESSRIGHTONLYME      = "Only me";

    // Define constant string for invite sport type
    public static final String OMEPARSEINVITESPORTTYPEBADMINTON     = "Badminton";
    public static final String OMEPARSEINVITESPORTTYPEBASKERBALL    = "Basketball";
    public static final String OMEPARSEINVITESPORTTYPESNOOKER       = "Snooker";
    public static final String OMEPARSEINVITESPORTTYPETABLEFOOTBALL = "Table football";
    public static final String OMEPARSEINVITESPORTTYPETABLETENNIS   = "Table Tennis";
    public static final String OMEPARSEINVITESPORTTYPETENNIS        = "Tennis";
    public static final String OMEPARSEINVITESPORTTYPERUNNING       = "Running";
    public static final String OMEPARSEINVITESPORTTYPECYCLING       = "Cycling";
    public static final String OMEPARSEINVITESPORTTYPEMAHJONG       = "Mahjong";
    public static final String OMEPARSEINVITESPORTTYEPOKER          = "Poker";
    public static final String OMEPARSEINVITESPORTTYPEOTHER         = "Other";

    // Defin constant string for group id
    // begin from 100
    public static final int OMEPARSEINVITEACCESSRIGHTGROUPID       = 100;
    public static final int OMEPARSEINVITESPORTTYPEGROUPID         = 101;
    public static final int OMEPARSEINVITEPLAYFEEGROUPID           = 102;

    // Define constant string for access right tag
    // begin from 110
    public static final int OMEPARSEINVITEACCESSRIGHTPUBLICID      = 110;
    public static final int OMEPARSEINVITEACCESSRIGHTFRIENDID      = 111;
    public static final int OMEPARSEINVITEACCESSRIGHTONLYMEID      = 112;

    // Define constant string for invite sport type tag
    // begin from 110
    public static final int OMEPARSEINVITESPORTTYPEBADMINTONID     = 110;
    public static final int OMEPARSEINVITESPORTTYPEBASKERBALLID    = 111;
    public static final int OMEPARSEINVITESPORTTYPESNOOKERID       = 112;
    public static final int OMEPARSEINVITESPORTTYPETABLEFOOTBALLID = 113;
    public static final int OMEPARSEINVITESPORTTYPETABLETENNISID   = 114;
    public static final int OMEPARSEINVITESPORTTYPETENNISID        = 115;
    public static final int OMEPARSEINVITESPORTTYPERUNNINGID       = 116;
    public static final int OMEPARSEINVITESPORTTYPECYCLINGID       = 117;
    public static final int OMEPARSEINVITESPORTTYPEMAHJONGID       = 118;
    public static final int OMEPARSEINVITESPORTTYPEPOKERID         = 119;
    public static final int OMEPARSEINVITESPORTTYPEOTHERID         = 120;


    // Define constant string for invite play fee tag
    public static final int OMEPARSEINVITEFEEDEFAULTID             = 110;
    public static final int OMEPARSEINVITEFEEFREEID                = 111;

    // Define constant string for invite placehold
    public static final String OMEPARSEINVITEPLAYTIMEPLACEHOLD     = "DD/MM HH";
    public static final String OMEPARSEINVITEPLAYCOURTPLACEHOLD    = "input court address";
    public static final String OMEPARSEINVITEPLAYOTHERPLACEHOLD    = "input other requirement";

    // Define constant string for invite default value






    // Define constant string for invite requirement
    public static final String OMEPARSEINVITEPLAYERALERTINFO         = "Do you really join this play?";
    public static final String OMEPARSEINVITEPLAYERINFOHeader        = "Player Info";
    public static final String OMEPARSEINVITEPLAYERINFOPLAYERNAME    = "Name";
    public static final String OMEPARSEINVITEPLAYERINFOPLAYERLEVEL   = "Level";
    public static final String OMEPARSEINVITEREQUIREMENTHEADER       = "Invite Requirement";
    public static final String OMEPARSEINVITEREQUIREMENTSPORTTYPE    = "Sport Type";
    public static final String OMEPARSEINVITEREQUIREMENTPLAYERNUMBER = "Number";
    public static final String OMEPARSEINVITEREQUIREMENTPLAYERLEVEL  = "Player Level";
    public static final String OMEPARSEINVITEREQUIREMENTTIME         = "Time";
    public static final String OMEPARSEINVITEREQUIREMENTCOURT        = "Court";
    public static final String OMEPARSEINVITEREQUIREMENTFEE          = "Fee";
    public static final String OMEPARSEINVITEREQUIREMENTOTHER        = "Other";
    public static final String OMEPARSEINVITEJOINPLAYHEADER          = "";
    public static final String OMEPARSEINVITEJOINPLAYBUTTON          = "Join Play";
    public static final String OMEPARSEINVITEPLAYERJOINPLAYSUCCESSALERTINFO = "Successfully submit request, please wait for reply!";

    public static final String OMEPARSEMESSAGEJOINPLAYREQUESTFIRSTPART  = "Hi, ";
    public static final String OMEPARSEMESSAGEJOINPLAYREQUESTSECONDPART = " want to join play";
    public static final String OMEPARSEMESSAGEJOINPLAYREQUESTTHIRDPART  = " with you";
    public static final String OMEPARSEMESSAGEJOINPLAYNOTSELF           = "can not re-join yourself play!";





    // Define constant string for Facebook
    public static final String OMEPARSEFACEBOOKPUBLICPROFILE = "public_profile";
    public static final String OMEPARSEFACEBOOKEMAIL         = "email";
    public static final String OMEPARSEFACEBOOKUSERFRIENDS   = "user_friends";


    // Define constant string for setting view
    public static final String OMEPARSESETTINGLOGOUTALERTINFO= "this will not delete any data, You can still log in with this account.";
    public static final String OMEPARSEME                    = "Me";
    public static final String OMEPARSEFEEDBACK              = "Feedback";


    // Define constant string for login view
    public static final String OMEPARSELOGINALERTINFO    = "can not log in:\n The username or password is wrong.";

    // Define constant string for new user view
    public static final String OMEPARSESIGUP             = "Signing Up";

    // Define constant variable for storyboard
    public static final int OMEMESSAGECELLTAG            = 101;
    public static final int OMEMESSAGECELLAVATARIMAGETAG = 102;
    public static final int OMEMESSAGECELLNAMETAG        = 103;
    public static final int OMEMESSAGECELLCONTENTTAG     = 104;
    public static final int OMEMESSAGECELLTIMETAG        = 105;

    // Define constant variable for chat view
    public static final String OMECHATVIEWTITLE             = "Local Invitation";

    // Define constant variable for message view
    public static final int    OMEMESSAGELISTLIMIT          = 100;
    public static final int    OMEMESSAGESEARCHLIMIT        = 100000;
    public static final String OMEMESSAGETITLE              = "Messages";
    public static final String OMEMESSAGESENDBUTTONTITLE    = "Send";
    public static final String OMEMESSAGEINPUTTEXTPLACEHOLD = "Reply message";
    public static final String OMEMESSAGENOUSERINFO         = "This username is null, no message";
    public static final String OMEMESSAGELISTNOFORUSER      = "No message to you now";


    // Define constant variable for cell
    public static final String OMEMESSAGECELL = "OMEMessageCell";
    public static final String OMEMEMECELL    = "OMEMemeCell";

    // Define constant variable for feedback view
    public static final String OMEFEEDBACKTITLE       = "Feedback";
    public static final String OMEFEEDBACKRECEIVEINFO = "ToPlay Team has received your feedback,Thanks again!";
    public static final String OMEFEEDBACKPLACEHOLD   = "Thank you for your feedback and suggestion, ToPlay team will follow your feedback and suggestion to improve ToPlay app perfomance!\n\n                   ToPlay Team";

    // Define constant variable for me activity
    public static final String OMEPARSEPARENTCLASSNAME              = "ParentClassName";
    public static final String OMEPARSELOCALWITHOUTMAPACTIVITYCLASS = "com.oneme.toplay.local.LocalWithoutMapActivity";


    //Your other constants, if you have them..

    // Define constant variable for invoke activity
    public static final String OMEPARSEINVOKECALLPHONE = "tel:";

    // Define constant variable for Map

    public static final String PLACE_API_BASE            = "https://maps.googleapis.com/maps/api/place";
    public static final String PLACE_TYPE_AUTOCOMPLETE   = "/autocomplete";
    public static final String PLACE_TYPE_NEARBY         = "/nearbysearch";
    public static final String PLACE_KEY                 = "?key=";
    public static final String PLACE_INPUT               = "&input=";
    public static final String PLACE_RESPONSE_PREDICTION = "predictions";
    public static final String PLACE_PREDICTION_TERM     = "terms";
    public static final String PLACE_TERM_VALUE          = "value";
    public static final String PLACE_OUT_JSON            = "/json";
    public static final String PLACE_OUT_ENCODE          = "utf8";
    public static final String PLACE_API_KEY             = AppConstant.OMETOPLAYGOOGLEPLACEKEY;
    public static final String PLACE_LOCATION            = "&location=";
    public static final String PLACE_LANGUAGE            = "&language=";
    public static final String PLACE_RADIUS              = "&radius=";
    public static final String OME_RADIUS                = "5000";
    public static final String PLACE_RESPONSE_RESULTS    = "results";
    public static final String PLACE_RESULTS_NAME        = "name";
    public static final String PLACE_RESULTS_VICINITY    = "vicinity";
    public static final String PLACE_SEARCH_STATUS       = "status";
    public static final String PLACE_SEARH_OK            = "OK";
    public static final String PLACE_TYPE_KEY            = "&types=";
    public static final String PLACE_TYPES               = "gym|stadium|amusement_park|university|park";





    public static final String BD_PLACES_API_SUGGESTION  = "http://api.map.baidu.com/place/v2/suggestion";
    public static final String BD_PLACES_API_SEARCH      = "http://api.map.baidu.com/place/v2/search";
    public static final String BD_GEOCODER_API           = "http://api.map.baidu.com/geocoder/v2";
    public static final String BD_PLACE_API_KEY          = AppConstant.OMETOPLAYBAIDUPLACEKEY;
    public static final String BD_PLACE_OUT_JSON         = "&output=json";
    public static final String BD_PLACE_REGION           = "&region=全国";
    public static final String BD_PLACE_QUERY            = "?query=";
    public static final String BD_PLACE_KEY              = "&ak=";
    public static final String BD_PLACE_RESULT           = "result";
    public static final String BD_PLACE_RESULTS          = "results";
    public static final String BD_PLACE_NAME             = "name";
    public static final String BD_PLACE_STATUS           = "status";
    public static final String BD_PLACE_CITY             = "city";
    public static final String BD_PLACE_DISTRICT         = "district";
    public static final String BD_PLACE_STREET           = "street";
    public static final String BD_PLACE_STREET_NUM       = "street_number";
    public static final String BD_PLACE_LOCATION         = "&location=";
    public static final String BD_PLACE_RADIUS           = "&radius=";
    public static final String BD_PLACE_NEARBY_SEARCH    = "运动$羽毛球$网球$健身$篮球$台球$山$森林$公园";//"小区$公司$街$路$银行$酒店$区";
    public static final String BD_PLACE_SEARCH_ADDRESS   = "小区$公司$街$路$银行$酒店$区";
    public static final String BD_PLACE_ADDRESS          = "address";
    public static final String BD_GEOCODER_KEY           = "/?ak=";
    public static final String BD_GEOCODER_API_KEY       = AppConstant.OMETOPLAYBAIDUPLACEKEY;
    public static final String BD_GEOCODER_CALLBACK      = "&callback=renderReverse";
    public static final String BD_GEOCODER_LOCATION      = "&location=";
    public static final String BD_GEOCODER_OUT_JSON      = "&output=json";
    public static final String BD_GEOCODER_POIS          = "&pois=0";
    public static final String BD_GEOCODER_STATUS        = "status";
    public static final String BD_GEOCODER_RESULT        = "result";

    public static final String BD_GEOCODER_FORMATTED_ADDRESS = "formatted_address";
    public static final String BD_GEOCODER_ADDRESS_COMPONENT = "addressComponent";
    public static final String BD_GEOCODER_CITY              = "city";


    // Define constant variable for json request return
    public static final String SPORT_JSON_3RD           = "3rd";
    public static final String SPORT_JSON_PAYNO         = "paynumber";
    public static final String SPORT_JSON_SMSCODE       = "smscode";
    public static final String SPORT_JSON_ERRCODE       = "errcode";
    public static final String SPORT_JSON_SUCCESS       = "0";

    // Used for lookup URI that contains an encoded JSON string.
    public static final String LOOKUP_URI_ENCODED = "encoded";

}
