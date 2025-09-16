package com.jetbrains.perfinsight.demobenchmark.scenario.chain;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;

import java.util.List;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

/**
 * The link: https://youtrack.logs.aws.intellij.net/_dashboards/app/dashboards#/view/2be4b2a0-58f7-11f0-a924-81b0865f61d1?_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:'2025-09-01T06:00:00.000Z',to:'2025-09-03T21:00:00.000Z'))&_a=(description:'',filters:!(('$state':(store:appState),meta:(alias:'GET%20%2Fapi%2Fissues%2F%7BID%7D',disabled:!f,index:b6720360-d19d-11ee-ba2b-517929de00b6,key:query,negate:!f,type:custom,value:'%7B%22bool%22:%7B%22must%22:%5B%7B%22query_string%22:%7B%22query%22:%22request_method.keyword:GET%20AND%20uri.keyword:%2F%5C%5C%2Fapi%5C%5C%2Fissues%5C%5C%2F%5B%5E%5C%5C%2F%5D%2B%2F%22,%22analyze_wildcard%22:true,%22time_zone%22:%22UTC%22%7D%7D%5D,%22filter%22:%5B%5D,%22should%22:%5B%5D,%22must_not%22:%5B%5D%7D%7D'),query:(bool:(filter:!(),must:!((query_string:(analyze_wildcard:!t,query:'request_method.keyword:GET%20AND%20uri.keyword:%2F%5C%2Fapi%5C%2Fissues%5C%2F%5B%5E%5C%2F%5D%2B%2F',time_zone:UTC))),must_not:!(),should:!()))),('$state':(store:appState),meta:(alias:!n,disabled:!f,index:b6720360-d19d-11ee-ba2b-517929de00b6,key:http_user_agent.keyword,negate:!f,params:(query:'Mozilla%2F5.0%20(Macintosh;%20Intel%20Mac%20OS%20X%2010_15_7)%20AppleWebKit%2F537.36%20(KHTML,%20like%20Gecko)%20Chrome%2F139.0.0.0%20Safari%2F537.36'),type:phrase),query:(match_phrase:(http_user_agent.keyword:'Mozilla%2F5.0%20(Macintosh;%20Intel%20Mac%20OS%20X%2010_15_7)%20AppleWebKit%2F537.36%20(KHTML,%20like%20Gecko)%20Chrome%2F139.0.0.0%20Safari%2F537.36')))),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),query:(language:lucene,query:''),timeRestore:!t,title:'YouTrack%20Production%20Nginx%20Access%20Log%20Metrics%20by%20URL%20Templates',viewMode:view)
 *
 */
public class GET_api_issues_ID {
    public ChainBuilder build() {

        String top1_fields = "description,updater(@updater),creator(@updater),attachments(@attachments),workItems(id,author(@permittedUsers),creator(@permittedUsers),text,type(@value),duration(minutes,presentation),textPreview,created,updated,date,usesMarkdown,attributes(id,name,value(@value))),usesMarkdown,hasEmail,wikifiedDescription,messages,fields(@fields),tags(id,name,color(@color),isDeletable,isUpdatable,isUsable,query,issuesUrl,isShareable,pinnedByDefault,pinned,pinnedInHelpdesk,untagOnResolve,owner(@permittedUsers),readSharingSettings(@updateSharingSettings),tagSharingSettings(@updateSharingSettings),updateSharingSettings(@updateSharingSettings),sortOrder(isSortable)),pinnedComments(author(@permittedUsers),id,text,textPreview,deleted,pinned,visibility(@visibility),attachments(@attachments),reactions(@reactions),reactionOrder,usesMarkdown,hasEmail,canUpdateVisibility,suspiciousEmail,created,updated,mentionedUsers(@updater),mentionedIssues(@mentionedIssues),mentionedArticles(@mentionedArticles),issue(@issue),markdownEmbeddings(@markdownEmbeddings)),canUpdateVisibility,canAddPublicComment,project(id,ringId,name,shortName,projectType(id),pinned,iconUrl,template,archived,hasArticles,isDemo,sourceTemplate,fieldsSorted,query,issuesUrl,restricted,leader(@permittedUsers),creationTime,widgets(@widgets),team(id),plugins(timeTrackingSettings(id,enabled),helpDeskSettings(id,defaultForm(uuid,title)),vcsIntegrationSettings(hasVcsIntegrations),grazie(disabled))),widgets(@widgets),externalIssue(key,name,url),summaryTextSearchResult(@textSearchResult),descriptionTextSearchResult(@textSearchResult),channel(id,name,$type,mailboxRule(id)),aiSuggestions(comment(id,draft(id,text,textPreview,deleted,pinned,visibility(@visibility),attachments(@attachments),reactions(@reactions),reactionOrder,usesMarkdown,hasEmail,canUpdateVisibility,suspiciousEmail,created,updated,mentionedUsers(@updater),mentionedIssues(@mentionedIssues),mentionedArticles(@mentionedArticles),issue(@issue),markdownEmbeddings(@markdownEmbeddings))),duplicates(id,issues(@mentionedIssues),duplicatesRoot(@mentionedIssues))),id,idReadable,summary,reporter(@updater),resolved,updated,created,unauthenticatedReporter,visibility(@visibility),votes,voters(hasVote),watchers(hasStar),usersTyping(@usersTyping),canUndoComment,mentionedUsers(@updater),mentionedIssues(@mentionedIssues),mentionedArticles(@mentionedArticles),markdownEmbeddings(@markdownEmbeddings);@mentionedIssues:id,idReadable,summary,reporter(@updater),updater(@updater),resolved,updated,created,unauthenticatedReporter,fields(@fields),project(@project),visibility(@visibility),tags(@tags),votes,voters(hasVote),watchers(hasStar),usersTyping(@usersTyping),canUndoComment;@attachments:id,name,author(id,ringId,avatarUrl,canReadProfile,isLocked,login,name),created,updated,mimeType,url,size,visibility(@visibility),imageDimensions(width,height),thumbnailURL,recognizedText,searchResults(textSearchResult(highlightRanges(@textRange))),comment(id,visibility(@visibility)),embeddedIntoDocument(id),embeddedIntoComments(id);@fields:value(id,minutes,presentation,isEstimation,isSpentTime,name,description,localizedName,isResolved,color(@color),buildIntegration,buildLink,text,ringId,login,email,isEmailVerified,guest,fullName,avatarUrl,online,banned,banBadge,canReadProfile,isLocked,userType(id),issueRelatedGroup(@permittedGroups),allUsersGroup,$type(),icon,teamForProject(name,shortName)),id,$type,hasStateMachine,isUpdatable,projectCustomField($type,id,field(id,name,ordinal,aliases,localizedName,fieldType(id,presentation,isBundleType,valueType,isMultiValue)),bundle(id,$type),canBeEmpty,emptyFieldText,hasRunningJob,ordinal,isSpentTime,isEstimation,isPublic),visibleOnList,searchResults(id,textSearchResult(@textSearchResult)),pausedTime;@mentionedArticles:id,idReadable,reporter(@permittedUsers),summary,project(@project),parentArticle(idReadable),ordinal,visibility(@visibility),hasUnpublishedChanges,hasChildren,tags(@tags),hasStar;@visibility:$type,implicitPermittedUsers(@permittedUsers),permittedGroups(@permittedGroups),permittedUsers(@permittedUsers);@widgets:id,key,appId,description,appName,appTitle,name,collapsed,configurable,indexPath,extensionPoint,iconPath,guard,appIconPath,appDarkIconPath,defaultHeight,defaultWidth,expectedHeight,expectedWidth,vendorName,vendorEmail,vendorUrl,marketplaceId;@updateSharingSettings:permittedGroups(@permittedGroups),permittedUsers(@permittedUsers);@updater:id,ringId,login,name,email,isEmailVerified,guest,fullName,avatarUrl,online,banned,banBadge,canReadProfile,isLocked,userType(id),issueRelatedGroup(@permittedGroups);@reactions:id,reaction,author(@permittedUsers);@usersTyping:timestamp,user(@permittedUsers);@value:id,name,autoAttach,description,hasRunningJobs,color(@color),attributes(id,timeTrackingSettings(id,project(id)));@permittedUsers:id,ringId,login,name,email,isEmailVerified,guest,fullName,avatarUrl,online,banned,banBadge,canReadProfile,isLocked,userType(id);@project:id,ringId,name,shortName,projectType(id),pinned,iconUrl,template,archived,hasArticles;@permittedGroups:id,name,ringId,allUsersGroup,$type(),icon,teamForProject(name,shortName);@tags:id,name,color(@color),isDeletable,isUpdatable,isUsable;@textSearchResult:highlightRanges(@textRange),textRange(@textRange);@color:id,background,foreground;@markdownEmbeddings:key,settings,widget(id);@textRange:startOffset,endOffset;@issue:$type,id,project(id)";
        ChainBuilder getIssue_top1_2391 = group("TOP-1").on(
            http("GET /api/issues/{ID}")
                .get("/api/issues/#{ISSUE_ID}")
                .header("Authorization", "Bearer #{token}")
                .queryParamSeq(List.of(
                    Map.entry("$top", -1),
                    Map.entry("fields", top1_fields)
                ))

        );
        ;

        ChainBuilder getIssue_top2_1499 = group("TOP-1 guest").on(
            http("GET /api/issues/{ID}")
                .get("/api/issues/#{ISSUE_ID}")
                .queryParamSeq(List.of(
                    Map.entry("$top", -1),
                    Map.entry("fields", top1_fields),
                    Map.entry("isGuest", true)
                )));

        String top3_fields = "allMentions(@container),mentionedIn(container(@container),source($type));@container:id,idReadable,reporter(id,ringId,login,name,email,isEmailVerified,guest,fullName,avatarUrl,online,banned,banBadge,canReadProfile,isLocked,userType(id),issueRelatedGroup(@issueRelatedGroup)),summary,project(id,ringId,name,shortName,projectType(id),pinned,iconUrl,template,archived,hasArticles),parentArticle(idReadable),ordinal,visibility($type,implicitPermittedUsers(@permittedUsers),permittedGroups(@issueRelatedGroup),permittedUsers(@permittedUsers)),hasUnpublishedChanges,hasChildren,tags(id,name,color(@color),isDeletable,isUpdatable,isUsable),hasStar,resolved,fields(value(id,minutes,presentation,isEstimation,isSpentTime,name,description,localizedName,isResolved,color(@color),buildIntegration,buildLink,text,ringId,login,email,isEmailVerified,guest,fullName,avatarUrl,online,banned,banBadge,canReadProfile,isLocked,userType(id),issueRelatedGroup(@issueRelatedGroup),allUsersGroup,$type(),icon,teamForProject(name,shortName)),id,$type,hasStateMachine,isUpdatable,projectCustomField($type,id,field(id,name,ordinal,aliases,localizedName,fieldType(id,presentation,isBundleType,valueType,isMultiValue)),bundle(id,$type),canBeEmpty,emptyFieldText,hasRunningJob,ordinal,isSpentTime,isEstimation,isPublic),visibleOnList,searchResults(id,textSearchResult(highlightRanges(@textRange),textRange(@textRange))),pausedTime),watchers(hasStar);@permittedUsers:id,ringId,login,name,email,isEmailVerified,guest,fullName,avatarUrl,online,banned,banBadge,canReadProfile,isLocked,userType(id);@issueRelatedGroup:id,name,ringId,allUsersGroup,$type(),icon,teamForProject(name,shortName);@color:id,background,foreground;@textRange:startOffset,endOffset";
        ChainBuilder getIssue_top3_1443 = group("TOP-3").on(
            http("GET /api/issues/{ID}")
                .get("/api/issues/#{ISSUE_ID}")
                .header("Authorization", "Bearer #{token}")
                .queryParamSeq(List.of(
                    Map.entry("$top", -1),
                    Map.entry("fields", top3_fields),
                    Map.entry("customFields", "Priority")
                )));

        String top4_fields = "draftComment(id,text,textPreview,deleted,pinned,visibility(@visibility),attachments(id,name,author(id,ringId,avatarUrl,canReadProfile,isLocked,login,name),created,updated,mimeType,url,size,visibility(@visibility),imageDimensions(width,height),thumbnailURL,recognizedText,searchResults(textSearchResult(highlightRanges(@textRange))),comment(id,visibility(@visibility)),embeddedIntoDocument(id),embeddedIntoComments(id)),reactions(id,reaction,author(@permittedUsers)),reactionOrder,usesMarkdown,hasEmail,canUpdateVisibility,suspiciousEmail,created,updated,mentionedUsers(@updater),mentionedIssues(id,idReadable,summary,reporter(@updater),updater(@updater),resolved,updated,created,unauthenticatedReporter,fields(value(id,minutes,presentation,isEstimation,isSpentTime,name,description,localizedName,isResolved,color(@color),buildIntegration,buildLink,text,ringId,login,email,isEmailVerified,guest,fullName,avatarUrl,online,banned,banBadge,canReadProfile,isLocked,userType(id),issueRelatedGroup(@permittedGroups),allUsersGroup,$type(),icon,teamForProject(name,shortName)),id,$type,hasStateMachine,isUpdatable,projectCustomField($type,id,field(id,name,ordinal,aliases,localizedName,fieldType(id,presentation,isBundleType,valueType,isMultiValue)),bundle(id,$type),canBeEmpty,emptyFieldText,hasRunningJob,ordinal,isSpentTime,isEstimation,isPublic),visibleOnList,searchResults(id,textSearchResult(highlightRanges(@textRange),textRange(@textRange))),pausedTime),project(@project),visibility(@visibility),tags(@tags),votes,voters(hasVote),watchers(hasStar),usersTyping(timestamp,user(@permittedUsers)),canUndoComment),mentionedArticles(id,idReadable,reporter(@permittedUsers),summary,project(@project),parentArticle(idReadable),ordinal,visibility(@visibility),hasUnpublishedChanges,hasChildren,tags(@tags),hasStar),issue($type,id,project(id)),markdownEmbeddings(key,settings,widget(id)));@visibility:$type,implicitPermittedUsers(@permittedUsers),permittedGroups(@permittedGroups),permittedUsers(@permittedUsers);@updater:id,ringId,login,name,email,isEmailVerified,guest,fullName,avatarUrl,online,banned,banBadge,canReadProfile,isLocked,userType(id),issueRelatedGroup(@permittedGroups);@permittedUsers:id,ringId,login,name,email,isEmailVerified,guest,fullName,avatarUrl,online,banned,banBadge,canReadProfile,isLocked,userType(id);@project:id,ringId,name,shortName,projectType(id),pinned,iconUrl,template,archived,hasArticles;@permittedGroups:id,name,ringId,allUsersGroup,$type(),icon,teamForProject(name,shortName);@tags:id,name,color(@color),isDeletable,isUpdatable,isUsable;@color:id,background,foreground;@textRange:startOffset,endOffset";
        ChainBuilder getIssue_top4_1438 = group("TOP-4").on(
            http("GET /api/issues/{ID}")
                .get("/api/issues/#{ISSUE_ID}")
                .header("Authorization", "Bearer #{token}")
                .queryParamSeq(List.of(
                    Map.entry("fields", top4_fields)
                )));
        ChainBuilder getIssue_top5_1264 = group("TOP-4 guest").on(
            http("GET /api/issues/{ID}")
                .get("/api/issues/#{ISSUE_ID}")
                .queryParamSeq(List.of(
                    Map.entry("fields", top4_fields),
                    Map.entry("isGuest", true)
                )));

        ChainBuilder getIssue_top6_1264 = group("TOP-3 guest").on(
            http("GET /api/issues/{ID}")
                .get("/api/issues/#{ISSUE_ID}")
                .queryParamSeq(List.of(
                    Map.entry("$top", -1),
                    Map.entry("fields", top3_fields),
                    Map.entry("isGuest", true)
                )));

        ChainBuilder getIssue_top7_259 = group("TOP-7").on(
            http("GET /api/issues/{ID}")
                .get("/api/issues/#{ISSUE_ID}")
                .header("Authorization", "Bearer #{token}")
                .queryParamSeq(List.of(
                    Map.entry("$top", -1),
                    Map.entry("referringQuery", ""),
                    Map.entry("fields", top1_fields)
                )));
        ChainBuilder getIssue_top7_16_guest = group("TOP-7 guest").on(
            http("GET /api/issues/{ID}")
                .get("/api/issues/#{ISSUE_ID}")
                .queryParamSeq(List.of(
                    Map.entry("$top", -1),
                    Map.entry("referringQuery", ""),
                    Map.entry("fields", top1_fields),
                    Map.entry("isGuest", true)
                )));
        ChainBuilder getIssue_top8_300 = group("TOP-8").on(
            http("GET /api/issues/{ID}")
                .get("/api/issues/#{ISSUE_ID}")
                .header("Authorization", "Bearer #{token}")
                .queryParamSeq(List.of(
                    Map.entry("$top", -1),
                    Map.entry("referringQuery", "#{REFERRING_QUERY}"),
                    Map.entry("fields", top1_fields)
                )));
        ChainBuilder getIssue_top8_50_guest = group("TOP-8 guest").on(
            http("GET /api/issues/{ID}")
                .get("/api/issues/#{ISSUE_ID}")
                .queryParamSeq(List.of(
                    Map.entry("$top", -1),
                    Map.entry("referringQuery", "#{REFERRING_QUERY}"),
                    Map.entry("fields", top1_fields),
                    Map.entry("isGuest", true)
                )));



        return group("GET /api/issues/{ID}").on(CoreDsl.randomSwitch().on(
                percent(100.0 * 2391 / 10000).then(getIssue_top1_2391),
                percent(100.0 * 1499 / 10000).then(getIssue_top2_1499),
                percent(100.0 * 1443 / 10000).then(getIssue_top3_1443),
                percent(100.0 * 1438 / 10000).then(getIssue_top4_1438),
                percent(100.0 * 1264 / 10000).then(getIssue_top5_1264),
                percent(100.0 * 1264 / 10000).then(getIssue_top6_1264),
                percent(100.0 * 259 / 10000).then(getIssue_top7_259),
                percent(100.0 * 16 / 10000).then(getIssue_top7_16_guest),
                percent(100.0 * 300 / 10000).then(getIssue_top8_300),
                percent(100.0 * 50 / 10000).then(getIssue_top8_50_guest)
            )
        );
    }
}
