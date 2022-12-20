package com.github.inf166.pluginphpstormtypo3committemplate

import com.github.inf166.pluginphpstormtypo3committemplate.components.Dialog
import com.github.inf166.pluginphpstormtypo3committemplate.helper.FormattedCommitMessage
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.Refreshable

class ShowCommitTemplateAction : AnAction(), DumbAware {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        val commitPanel = getCommitPanel(actionEvent) ?: return
        val intelliJcommitPannel: CommitMessageI = getCommitPanel(actionEvent) ?: return
        var commitMessageString = ""
        var prompt: Dialog
        if (commitPanel is CheckinProjectPanel) {
            commitMessageString = commitPanel.commitMessage
        }

        if(commitMessageString.length > 1) {
            var oldCommitMessage = FormattedCommitMessage(commitMessageString)
            prompt = Dialog(actionEvent.project, oldCommitMessage)
        } else {
            var emptyCommitMessage = FormattedCommitMessage()
            prompt = Dialog(actionEvent.project, emptyCommitMessage)
        }

        prompt.show()

        if (prompt.getExitCode() and DialogWrapper.OK_EXIT_CODE == 0) {
            intelliJcommitPannel.setCommitMessage(prompt.getCommitMessage().getFormattedCommitMessage())
        }
    }

    companion object {
        private fun getCommitPanel(e: AnActionEvent?): CommitMessageI? {
            if (e == null) {
                return null
            }
            val data = Refreshable.PANEL_KEY.getData(e.dataContext)
            return if (data is CommitMessageI) {
                data
            } else VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(e.dataContext)
        }
    }
}