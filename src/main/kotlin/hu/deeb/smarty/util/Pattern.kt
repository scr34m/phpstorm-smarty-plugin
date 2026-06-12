package hu.deeb.smarty.util

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.jetbrains.smarty.lang.SmartyTokenTypes
import com.jetbrains.smarty.lang.psi.SmartyCompositeElementTypes

object Pattern {
    val filePattern: PsiElementPattern.Capture<PsiElement?>
        get() = PlatformPatterns.psiElement(SmartyTokenTypes.STRING_LITERAL).withParent(
            PlatformPatterns.psiElement(SmartyCompositeElementTypes.ATTRIBUTE_VALUE).withParent(
                PlatformPatterns.psiElement(SmartyCompositeElementTypes.ATTRIBUTE)
                    .withText(PlatformPatterns.string().contains("file="))
            )
        )

    val functionPattern: PsiElementPattern.Capture<PsiElement?>
        get() = PlatformPatterns.psiElement(SmartyTokenTypes.IDENTIFIER).withParent(
            PlatformPatterns.psiElement(SmartyCompositeElementTypes.FUNCTION_CALL)
        )

}