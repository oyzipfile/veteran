// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.syntax.parsers;

import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.module.Module;
import com.veteran.hack.command.syntax.SyntaxChunk;

public class ModuleParser extends AbstractParser
{
    @Override
    public String getChunk(final SyntaxChunk[] chunks, final SyntaxChunk thisChunk, final String[] values, final String chunkValue) {
        if (chunkValue == null) {
            return this.getDefaultChunk(thisChunk);
        }
        final Module chosen = ModuleManager.getModules().stream().filter(module -> module.getName().toLowerCase().startsWith(chunkValue.toLowerCase())).findFirst().orElse(null);
        if (chosen == null) {
            return null;
        }
        return chosen.getName().substring(chunkValue.length());
    }
}
