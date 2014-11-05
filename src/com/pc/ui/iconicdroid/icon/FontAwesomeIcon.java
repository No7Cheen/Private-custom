/*
 * Copyright (C) 2013 Artur Termenji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pc.ui.iconicdroid.icon;

import com.pc.ui.iconicdroid.util.TypefaceManager.IconicTypeface;

/**
 * A wrapper for Font Awesome icon font
 * (http://fortawesome.github.com/Font-Awesome/).
 */
public enum FontAwesomeIcon implements Icon {
	// @formatter:off
	
    GLASS(0xF000),
    MUSIC(0xF001),
    SEARCH(0xF002),
    ENVELOPE(0xF003),
    HEART(0xF004),
    STAR(0xF005),
    STAR_EMPTY(0xF006),
    USER(0xF007),
    FILM(0xF008),
    TH_LARGE(0xF009),
    TH(0xF00A),
    TH_LIST(0xF00B),
    OK(0xF00C),
    REMOVE(0xF00D),
    ZOOM_IN(0xF00E),
    ZOOM_OUT(0xF010),
    OFF(0xF011),
    SIGNAL(0xF012),
    COG(0xF013),
    TRASH(0xF014),
    HOME(0xF015),
    FILE(0xF016),
    TIME(0xF017),
    ROAD(0xF018),
    DOWNLOAD_ALT(0xF019),
    DOWNLOAD(0xF01A),
    UPLOAD(0xF01B),
    INBOX(0xF01C),
    PLAY_CIRCLE(0xF01D),
    REPEAT(0xF01E),
    REFRESH(0xF021),
    LIST_ALT(0xF022),
    LOCK(0xF023),
    FLAG(0xF024),
    HEADPHONES(0xF025),
    VOLUME_OFF(0xF026),
    VOLUME_DOWN(0xF027),
    VOLUME_UP(0xF028),
    QRCODE(0xF029),
    BARCODE(0xF02A),
    TAG(0xF02B),
    TAGS(0xF02C),
    BOOK(0xF02D),
    BOOKMARK(0xF02E),
    PRINT(0xF02F),
    CAMERA(0xF030),
    FONT(0xF031),
    BOLD(0xF032),
    ITALIC(0xF033),
    TEXT_HEIGHT(0xF034),
    TEXT_WIDTH(0xF035),
    ALIGN_LEFT(0xF036),
    ALIGN_CENTER(0xF037),
    ALIGN_RIGHT(0xF038),
    ALIGN_JUSTIFY(0xF039),
    LIST(0xF03A),
    INDENT_LEFT(0xF03B),
    INDENT_RIGHT(0xF03C),
    FACETIME_VIDEO(0xF03D),
    PICTURE(0xF03E),
    PENCIL(0xF040),
    MAP_MARKER(0xF041),
    ADJUST(0xF042),
    TINT(0xF043),
    EDIT(0xF044),
    SHARE(0xF045),
    CHECK(0xF046),
    MOVE(0xF047),
    STEP_BACKWARD(0xF048),
    FAST_BACKWARD(0xF049),
    BACKWARD(0xF04A),
    PLAY(0xF04B),
    PAUSE(0xF04C),
    STOP(0xF04D),
    FORWARD(0xF04E),
    FAST_FORWARD(0xF050),
    STEP_FORWARD(0xF051),
    EJECT(0xF052),
    CHEVRON_LEFT(0xF053),
    CHEVRON_RIGHT(0xF054),
    PLUS_SIGN(0xF055),
    MINUS_SIGN(0xF056),
    REMOVE_SIGN(0xF057),
    OK_SIGN(0xF058),
    QUESTION_SIGN(0xF059),
    INFO_SIGN(0xF05A),
    SCREENSHOT(0xF05B),
    REMOVE_CIRCLE(0xF05C),
    OK_CIRCLE(0xF05D),
    BAN_CIRCLE(0xF05E),
    ARROW_LEFT(0xF060),
    ARROW_RIGHT(0xF061),
    ARROW_UP(0xF062),
    ARROW_DOWN(0xF063),
    SHARE_ALT(0xF064),
    RESIZE_FULL(0xF065),
    RESIZE_SMALL(0xF066),
    PLUS(0xF067),
    MINUS(0xF068),
    ASTERISK(0xF069),
    EXCLAMATION_SIGN(0xF06A),
    GIFT(0xF06B),
    LEAF(0xF06C),
    FIRE(0xF06D),
    EYE_OPEN(0xF06E),
    EYE_CLOSE(0xF070),
    WARNING_SIGN(0xF071),
    PLANE(0xF072),
    CALENDAR(0xF073),
    RANDOM(0xF074),
    COMMENT(0xF075),
    MAGNET(0xF076),
    CHEVRON_UP(0xF077),
    CHEVRON_DOWN(0xF078),
    RETWEET(0xF079),
    SHOPPING_CART(0xF07A),
    FOLDER_CLOSE(0xF07B),
    FOLDER_OPEN(0xF07C),
    RESIZE_VERTICAL(0xF07D),
    RESIZE_HORIZONTAL(0xF07E),
    BAR_CHART(0xF080),
    TWITTER_SIGN(0xF081),
    FACEBOOK_SIGN(0xF082),
    CAMERA_RETRO(0xF083),
    KEY(0xF084),
    COGS(0xF085),
    COMMENTS(0xF086),
    THUMBS_UP(0xF087),
    THUMBS_DOWN(0xF088),
    STAR_HALF(0xF089),
    HEART_EMPTY(0xF08A),
    SIGNOUT(0xF08B),
    LINKEDIN_SIGN(0xF08C),
    PUSHPIN(0xF08D),
    EXTERNAL_LINK(0xF08E),
    SIGNIN(0xF090),
    TROPHY(0xF091),
    GITHUB_SIGN(0xF092),
    UPLOAD_ALT(0xF093),
    LEMON(0xF094),
    PHONE(0xF095),
    CHECK_EMPTY(0xF096),
    BOOKMARK_EMPTY(0xF097),
    PHONE_SIGN(0xF098),
    TWITTER(0xF099),
    FACEBOOK(0xF09A),
    GITHUB(0xF09B),
    UNLOCK(0xF09C),
    CREDIT_CARD(0xF09D),
    RSS(0xF09E),
    HDD(0xF0A0),
    BULLHORN(0xF0A1),
    BELL(0xF0A2),
    CERTIFICATE(0xF0A3),
    HAND_RIGHT(0xF0A4),
    HAND_LEFT(0xF0A5),
    HAND_UP(0xF0A6),
    HAND_DOWN(0xF0A7),
    CIRCLE_ARROW_LEFT(0xF0A8),
    CIRCLE_ARROW_RIGHT(0xF0A9),
    CIRCLE_ARROW_UP(0xF0AA),
    CIRCLE_ARROW_DOWN(0xF0AB),
    GLOBE(0xF0AC),
    WRENCH(0xF0AD),
    TASKS(0xF0AE),
    FILTER(0xF0B0),
    BRIEFCASE(0xF0B1),
    FULLSCREEN(0xF0B2),
    GROUP(0xF0C0),
    LINK(0xF0C1),
    CLOUD(0xF0C2),
    BEAKER(0xF0C3),
    CUT(0xF0C4),
    COPY(0xF0C5),
    PAPER_CLIP(0xF0C6),
    SAVE(0xF0C7),
    SIGN_BLANK(0xF0C8),
    REORDER(0xF0C9),
    LIST_UL(0xF0CA),
    LIST_OL(0xF0CB),
    STRIKETHROUGH(0xF0CC),
    UNDERLINE(0xF0CD),
    TABLE(0xF0CE),
    MAGIC(0xF0D0),
    TRUCK(0xF0D1),
    PINTEREST(0xF0D2),
    PINTEREST_SIGN(0xF0D3),
    GOOGLE_PLUS_SIGN(0xF0D4),
    GOOGLE_PLUS(0xF0D5),
    MONEY(0xF0D6),
    CARET_DOWN(0xF0D7),
    CARET_UP(0xF0D8),
    CARET_LEFT(0xF0D9),
    CARET_RIGHT(0xF0DA),
    COLUMNS(0xF0DB),
    SORT(0xF0DC),
    SORT_DOWN(0xF0DD),
    SORT_UP(0xF0DE),
    ENVELOPE_ALT(0xF0E0),
    LINKEDIN(0xF0E1),
    UNDO(0xF0E2),
    LEGAL(0xF0E3),
    DASHBOARD(0xF0E4),
    COMMENT_ALT(0xF0E5),
    COMMENTS_ALT(0xF0E6),
    BOLT(0xF0E7),
    SITEMAP(0xF0E8),
    UMBRELLA(0xF0E9),
    PASTE(0xF0EA),
    LIGHTBULB(0xF0EB),
    EXCHANGE(0xF0EC),
    CLOUD_DOWNLOAD(0xF0ED),
    CLOUD_UPLOAD(0xF0EE),
    USER_MD(0xF0F0),
    STETHOSCOPE(0xF0F1),
    SUITCASE(0xF0F2),
    BELL_ALT(0xF0F3),
    COFFEE(0xF0F4),
    FOOD(0xF0F5),
    FILE_ALT(0xF0F6),
    BUILDING(0xF0F7),
    HOSPITAL(0xF0F8),
    AMBULANCE(0xF0F9),
    MEDKIT(0xF0FA),
    FIGHTER_JET(0xF0FB),
    BEER(0xF0FC),
    H_SIGN(0xF0FD),
    PLUS_SIGN_ALT(0xF0FE),
    DOUBLE_ANGLE_LEFT(0xF100),
    DOUBLE_ANGLE_RIGHT(0xF101),
    DOUBLE_ANGLE_UP(0xF102),
    DOUBLE_ANGLE_DOWN(0xF103),
    ANGLE_LEFT(0xF104),
    ANGLE_RIGHT(0xF105),
    ANGLE_UP(0xF106),
    ANGLE_DOWN(0xF107),
    DESKTOP(0xF108),
    LAPTOP(0xF109),
    TABLET(0xF10A),
    MOBILE_PHONE(0xF10B),
    CIRCLE_BLANK(0xF10C),
    QUOTE_LEFT(0xF10D),
    QUOTE_RIGHT(0xF10E),
    SPINNER(0xF110),
    CIRCLE(0xF111),
    REPLY(0xF112),
    GITHUB_ALT(0xF113),
    FOLDER_CLOSE_ALT(0xF114),
    FOLDER_OPEN_ALT(0xF115);
    
 // @formatter:on

	private final int mIconUtfValue;

	private FontAwesomeIcon(int iconUtfValue) {
		mIconUtfValue = iconUtfValue;
	}

	@Override
	public IconicTypeface getIconicTypeface() {
		return IconicTypeface.FONT_AWESOME;
	}

	@Override
	public int getIconUtfValue() {
		return mIconUtfValue;
	}
}
