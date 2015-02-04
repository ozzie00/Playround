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
    public static final String OMETOPLAYGOOGLEPLACEKEY     = "AIzaSyAWF807dWOPwod6y9-GIlnQgeARbA2BKug";//"AIzaSyDoh6RGtWqEro0UtA2BqSur-GESBuFngss";

    public static final int OMEINVITEOTHERMAXIMUMCHARACTERCOUNT = 140;

    public static final double OMEFEETTOMETERS                = 0.3048; // this is an exact value.
    public static final double OMEFEETTOMILES                 = 5280.0; // this is an exact value.
    public static final double OMEINVITEMAXIMUMSEARCHDISTANCE = 100.0;
    public static final double OMEMETERSINAKILOMETER          = 1000.0; // this is an exact value.
    public static final int OMEINVITESEARCHLIMIT              = 50; // query limit for pins and tableviewcells
    public static final int OMEINVITEREQUIREMENTSEARCHLIMIT   = 100;
    public static final int OMEPHONEMINIMUMLENGTH             = 3;

    // default color value
    public static final int OMETOPLAYDEFAULTCOLOR             = 0xFF1DA155;
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
    public static final String OMETOPLAYINVITECLASSKEY     = "ToPlayInvite";
    public static final String OMETOPLAYMESSAGECLASSKEY    = "ToPlayMessage";
    public static final String OMETOPLAYJOINCLASSKEY       = "ToPlayJoin";
    public static final String OMETOPLAYPLAYERCLASSKEY     = "ToPlayPlayer";
    public static final String OMETOPLAYVENUECLASSKEY      = "ToPlayVenue";

    // Parse API key constants related to user :
    public static final String OMEPARSEUSERKEY             = "user";
    public static final String OMEPARSEUSERSKEY            = "users";
    public static final String OMEPARSEUSEROMEIDKEY        = "omeID";
    public static final String OMEPARSEUSERNAMEKEY         = "username";
    public static final String OMEPARSEUSERLEVELKEY        = "userLevel";
    public static final String OMEPARSEUSERICONKEY         = "userIcon";
    public static final String OMEPARSECREATEDAT           = "createdAt";
    public static final String OMEPARSEUSERPASSWORDKEY     = "password";
    public static final String OMEPARSEUSERALIASKEY        = "userAlias";
    public static final String OMEPARSEUSERICONFILENAME    = "userIcon.png";
    public static final String OMEPARSEUSEROMEIDNULL       = "0";
    public static final int OMEPARSEAVATARFILETYPEPNG      = 1;
    public static final String OMEPARSEUSERHOMEVENUEKEY    = "userHomeVenue";
    public static final String OMEPARSEUSERBACKUPVENUEKEY  = "userBackupVenue";
    public static final String OMEPARSEUSERHOMEVENUEPHONEKEY    = "userHomeVenuePhone";
    public static final String OMEPARSEUSERBACKUPVENUEPHONEKEY  = "userBackupVenuePhone";

    // Parse API key constants for user
    public static final String OMEPARSEUSERPROFILENAMEKEY  = "userprofilename";
    public static final int OMEPARSEUSERPROFILENAMESUFFIXLENGTH = 6;
    public static final String OMEPARSEUSERPROFILEAVATARKEY  = "userprofileavatar";
    public static final int OMEPARSEUSERNAMEMINIMUMLENGTH     = 6;
    public static final int OMEPARSEUSERPASSWORDMINIMUMLENGTH = 6;
    public static final int OMEPARSEUSERVENUEASHOMERESULT     = 0;
    public static final int OMEPARSEUSERVENUEASBACKUPRESULT   = 1;
    public static final int OMEPARSEUSERAVATARCOMPRESSQUILTY  = 90;


    public static final String OMEPARSEUSERLASTTIMEKEY     = "userlasttime";
    public static final String OMEPARSEUSERLASTLOCATIONKEY = "userlastlocation";
    public static final String OMEPARSEUSERQRCODEKEY       = "userqrcode";
    public static final String OMEPARSEUSERTAGKEY          = "usertag";
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
    public static final String OMETOPLAYVENUENAMEKEY       = "venueName";
    public static final String OMETOPLAYVENUEICONKEY       = "venueIcon";
    public static final String OMETOPLAYVENUELEVELKEY      = "venueLevel";
    public static final String OMETOPLAYVENUETYPEKEY       = "venueType";
    public static final String OMETOPLAYVENUEADDRESSKEY    = "venueAddress";
    public static final String OMETOPLAYVENUELOCATIONKEY   = "venueLocation";
    public static final String OMETOPLAYVENUEPHONEKEY      = "venuePhone";
    public static final String OMETOPLAYVENUECOURTNUMBERKEY= "venueCourtNumber";
    public static final String OMETOPLAYVENUELIGHTEDKEY    = "venueLighted";
    public static final String OMETOPLAYVENUEINDOORKEY     = "venueIndoor";
    public static final String OMETOPLAYVENUEPUBLICKEY     = "venuePublic";
    public static final String OMETOPLAYVENUEUPLOADEDBYKEY = "venueUploadedby";
    public static final String OMETOPLAYVENUEPLAYERASHOMEKEY= "venuePlayerAsHome";



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


    // NSNotification userInfo keys:
    public static final String OMEFILTERDISTANCEKEY = "filterDistance";
    public static final String OMELOCATIONKEY       = "location";

   // Define common constant string
    public static final String OMEPARSENULLSTRING           = "";
    public static final int OMEPARSEINVITETIMETWOBITDIVIDER = 10;
    public static final int OMEPARSEINVITEYEARSTART         = 2014;
    public static final int OMEPARSEINVITEYEAREND            = 2035;

    // Define constant variable for me activity
    public static final String OMEPARSEPARENTCLASSNAME              = "ParentClassName";
    public static final String OMEPARSELOCALWITHOUTMAPACTIVITYCLASS = "com.oneme.toplay.local.LocalWithoutMapActivity";

    // Define constant variable for invoke activity
    public static final String OMEPARSEINVOKECALLPHONE = "tel:";


    //Your other constants, if you have them..

}
