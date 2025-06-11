package com.flippingcopilot.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data; // Ensure @Data is imported
import lombok.Getter; // Explicitly import @Getter for clarity, though @Data includes it
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.runelite.api.GrandExchangeOfferState;

import java.time.Instant;
import java.util.Objects;

@Data // This annotation should generate getters/setters/equals/hashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter // Explicitly declare if @Data isn't enough, though usually it is.
public class Offer {
    private String id; // This is the field getId() is for
    private OfferStatus offerStatus;
    private int itemId;
    private int price;
    private int quantity;
    private int slot;
    private boolean copilotPriceUsed;
    private Instant time;
    private int totalQuantity;
    private boolean wasCopilotSuggestion;
    private boolean acked;

    // These fields are part of the update method, not direct constructor params
    private String itemName;
    private long spent;
    private int collected;
    private GrandExchangeOfferState state;


    // If @Data or @Getter isn't generating it, uncomment this explicit getter:
    // public String getId() {
    //     return id;
    // }

    public boolean isAcked() {
        return acked;
    }

    public boolean isFreeSlot() {
        return state == GrandExchangeOfferState.EMPTY || state == GrandExchangeOfferState.CANCELLED_BUY || state == GrandExchangeOfferState.CANCELLED_SELL;
    }

    public void update(
            String itemName, int price, int quantity, long spent, int collected,
            GrandExchangeOfferState state, Instant time, int totalQuantity,
            boolean copilotPriceUsed, boolean wasCopilotSuggestion) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.spent = spent;
        this.collected = collected;
        this.state = state;
        this.time = time;
        this.totalQuantity = totalQuantity;
        this.copilotPriceUsed = copilotPriceUsed;
        this.wasCopilotSuggestion = wasCopilotSuggestion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Offer other = (Offer) obj;
        return itemId == other.itemId &&
                price == other.price &&
                quantity == other.quantity &&
                slot == other.slot &&
                copilotPriceUsed == other.copilotPriceUsed &&
                totalQuantity == other.totalQuantity &&
                wasCopilotSuggestion == other.wasCopilotSuggestion &&
                acked == other.acked &&
                spent == other.spent &&
                collected == other.collected &&
                offerStatus == other.offerStatus && // Compare enums
                state == other.state && // Compare enums
                Objects.equals(id, other.id) && // Compare String IDs
                Objects.equals(itemName, other.itemName) && // Compare String item names
                Objects.equals(time, other.time); // Compare Instant timestamps
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, offerStatus, itemId, itemName, price, quantity, slot, copilotPriceUsed, time, totalQuantity, wasCopilotSuggestion, acked, spent, collected, state);
    }
}
