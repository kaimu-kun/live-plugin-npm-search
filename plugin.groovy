import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import groovy.json.JsonSlurper

import static liveplugin.PluginUtil.*

registerAction("NPMSearchAction", "ctrl shift X") { AnActionEvent event ->
    Closure itemProvider = { String pattern, ProgressIndicator indicator ->
        if(pattern.length() < 2) return []

        googleSuggestionsFor(pattern)
    }
    showPopupSearch("NPM quick search", event.project, itemProvider) { String item ->
        BrowserUtil.open("https://www.npmjs.com/package/${item}")
    }a
}


static List<String> googleSuggestionsFor(String text) {
    text = URLEncoder.encode(text, "UTF-8")
    def json = "https://www.npmjs.com/search/suggestions?q=$text".toURL().text
    new JsonSlurper().parseText(json).collect {
        item -> return item.name
    }
}

if (!isIdeStartup) show("Loaded NPMSearchAction<br/>Use ctrl+shift+X to run it")
