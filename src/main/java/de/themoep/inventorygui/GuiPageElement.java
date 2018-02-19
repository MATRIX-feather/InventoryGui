package de.themoep.inventorygui;

/*
 * Copyright 2017 Max Lee (https://github.com/Phoenix616)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.bukkit.inventory.ItemStack;

/**
 * This is an element that allows for controlling the pagination of the gui.
 * <b>Untested und potentially unfinished.</b>
 */
public class GuiPageElement extends GuiStaticElement {
    private PageAction pageAction;

    /**
     * An element that allows for controlling the pagination of the gui.
     * @param slotChar      The character to replace in the gui setup string
     * @param item          The {@link ItemStack} representing this element
     * @param pageAction    What kind of page action you want to happen when interacting with the element.
     * @param text          The text to display on this element, placeholders are automatically
     *                      replaced, see {@link InventoryGui#replaceVars(String)} for a list of
     *                      the placeholder variables. Empty text strings are also filter out, use
     *                      a single space if you want to add an empty line!<br>
     *                      If it's not set/empty the item's default name will be used
     */
    public GuiPageElement(char slotChar, ItemStack item, PageAction pageAction, String... text) {
        super(slotChar, item, click -> {
            switch (pageAction) {
                case NEXT:
                    if (click.getGui().getPageNumber() + 1 < click.getGui().getPageAmount()) {
                        click.getGui().playClickSound();
                        click.getGui().setPageNumber(click.getGui().getPageNumber() + 1);
                    }
                    break;
                case PREVIOUS:
                    if (click.getGui().getPageNumber() > 0) {
                        click.getGui().playClickSound();
                        click.getGui().setPageNumber(click.getGui().getPageNumber() - 1);
                    }
                    break;
                case FIRST:
                    click.getGui().playClickSound();
                    click.getGui().setPageNumber(0);
                    break;
                case LAST:
                    click.getGui().playClickSound();
                    click.getGui().setPageNumber(click.getGui().getPageAmount() - 1);
                    break;
            }
            return true;
        }, text);
        this.pageAction = pageAction;
    }

    @Override
    public ItemStack getItem(int slot) {
        if ((pageAction == PageAction.NEXT && gui.getPageNumber() + 1 >= gui.getPageAmount())
                || (pageAction == PageAction.PREVIOUS && gui.getPageNumber() == 0)) {
            return gui.getFiller().getItem(slot);
        }
        try {
            if (pageAction == PageAction.PREVIOUS) {
                setNumber(gui.getPageNumber());
            } else if (pageAction == PageAction.NEXT) {
                setNumber(gui.getPageNumber() + 2);
            } else if (pageAction == PageAction.LAST) {
                setNumber(gui.getPageAmount());
            }
        } catch (IllegalArgumentException e) {
            // cannot set that item amount/number as it isn't supported in Minecraft
        }
        return super.getItem(slot).clone();
    }

    public enum PageAction {
        NEXT,
        PREVIOUS,
        FIRST,
        LAST;
    }
}
