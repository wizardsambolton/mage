/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package org.mage.test.cards.single.ths;

import mage.constants.CardType;
import mage.constants.PhaseStep;
import mage.constants.Zone;
import mage.game.permanent.Permanent;
import org.junit.Assert;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 *
 * @author LevelX2
 */
public class PurphorosGodOfTheForgeTest extends CardTestPlayerBase {
    /**
     * I had a situation come up today where I had a Purphoros on the field
     * and 5 devotion with an Eidolon and Phoenix. My opponent killed the 
     * Phoenix, but Purphoros still was "turned on".
     */
    @Test
    public void testFacedownNotCountedForDevotion1() {
        addCard(Zone.BATTLEFIELD, playerB, "Swamp", 5);
        addCard(Zone.HAND, playerB, "Reach of Shadows");
        
        // Indestructible
        // As long as your devotion to red is less than five, Purphoros isn't a creature.
        // Whenever another creature enters the battlefield under your control, Purphoros deals 2 damage to each opponent.
        // {2}{R}: Creatures you control get +1/+0 until end of turn.
        addCard(Zone.BATTLEFIELD, playerA, "Purphoros, God of the Forge");
        
        // Whenever a player casts a spell with converted mana cost 3 or less,
        // Eidolon of the Great Revel deals 2 damage to that player.
        addCard(Zone.BATTLEFIELD, playerA, "Eidolon of the Great Revel");
        // Flying
        // When Ashcloud Phoenix dies, return it to the battlefield face down.
        // Morph (You may cast this card face down as a 2/2 creature for . Turn it face up any time for its morph cost.)
        // When Ashcloud Phoenix is turned face up, it deals 2 damage to each player.
        addCard(Zone.BATTLEFIELD, playerA, "Ashcloud Phoenix");

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerB, "Reach of Shadows", "Ashcloud Phoenix");

        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();

        assertLife(playerA, 20);
        assertLife(playerB, 18); // 2 damage from the returning Phoenix
        
        assertGraveyardCount(playerB, "Reach of Shadows", 1);
        assertPermanentCount(playerA, "Ashcloud Phoenix", 0);
        assertPermanentCount(playerA, "face down creature", 1);

        Permanent purphorosGodOfTheForge = getPermanent("Purphoros, God of the Forge", playerA);
        Assert.assertFalse("Purphoros may not be a creature but it is", purphorosGodOfTheForge.getCardType().contains(CardType.CREATURE));
    }
    
    @Test
    public void testFacedownNotCountedForDevotion2() {       
        addCard(Zone.BATTLEFIELD, playerA, "Mountain", 3);
        // Indestructible
        // As long as your devotion to red is less than five, Purphoros isn't a creature.
        // Whenever another creature enters the battlefield under your control, Purphoros deals 2 damage to each opponent.
        // {2}{R}: Creatures you control get +1/+0 until end of turn.
        addCard(Zone.BATTLEFIELD, playerA, "Purphoros, God of the Forge");
        
        // Whenever a player casts a spell with converted mana cost 3 or less,
        // Eidolon of the Great Revel deals 2 damage to that player.
        addCard(Zone.BATTLEFIELD, playerA, "Eidolon of the Great Revel");
        // Flying
        // When Ashcloud Phoenix dies, return it to the battlefield face down.
        // Morph (You may cast this card face down as a 2/2 creature for {3}. Turn it face up any time for its morph cost.)
        // When Ashcloud Phoenix is turned face up, it deals 2 damage to each player.
        addCard(Zone.HAND, playerA, "Ashcloud Phoenix");


        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Ashcloud Phoenix");
        setChoice(playerA, "Yes"); // cast it face down as 2/2 creature

        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();

        assertPermanentCount(playerA, "Ashcloud Phoenix", 0);
        assertPermanentCount(playerA, "face down creature", 1);

        assertLife(playerA, 18); // 2 damage from Eidolon of the Great Revel
        assertLife(playerB, 18); // 2 damage from Purphoros for the morphed Phoenix
        
        Permanent purphorosGodOfTheForge = getPermanent("Purphoros, God of the Forge", playerA);
        Assert.assertFalse("Purphoros may not be a creature but it is", purphorosGodOfTheForge.getCardType().contains(CardType.CREATURE));
    }    
}
