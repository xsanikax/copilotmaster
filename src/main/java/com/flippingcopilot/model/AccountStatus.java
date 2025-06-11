package com.flippingcopilot.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson; // FIX: Add Gson import
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.runelite.api.GrandExchangeOfferState; // FIX: Add GrandExchangeOfferState import

import java.util.Objects;
// import java.util.UUID; // Removed this import previously if not needed

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AccountStatus {
    @SerializedName("account_hash")
    private Long accountHash;
    @SerializedName("item_id")
    private int itemId;
    private int price;
    private int quantity;
    @SerializedName("amount_spent")
    private long spent;
    @SerializedName("collected")
    private int collected;
    @SerializedName("offer_state")
    private GrandExchangeOfferState offerState;
    @SerializedName("offer_total_quantity")
    private int offerTotalQuantity;
    @SerializedName("copilot_price_used")
    private boolean copilotPriceUsed;
    @SerializedName("was_copilot_suggestion")
    private boolean wasCopilotSuggestion;
    @SerializedName("current_cash_stack")
    private long currentCashStack;
    @SerializedName("is_suggestion_skipped")
    private boolean isSuggestionSkipped;
    private int slot;
    private OfferStatus status;

    public boolean currentlyFlipping() {
        return this.quantity > 0 || (this.collected > 0 && isBuy());
    }

    public boolean isBuy() {
        return this.status == OfferStatus.BUY;
    }

    public boolean isCollectNeeded(Suggestion suggestion) {
        return (this.collected > 0 && this.isBuy()) || (this.collected < this.spent && this.isSell())
                || (suggestion != null && suggestion.getType() == "collect_cash" && this.isSell())
                || (suggestion != null && suggestion.getType() == "collect_items" && this.isBuy());
    }

    public boolean isSell() {
        return this.status == OfferStatus.SELL;
    }

    public long currentCashStack() {
        return currentCashStack;
    }

    public JsonObject toJson(Gson gson, boolean grandExchangeOpen, boolean isPriceGraphWebsite) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("account_hash", accountHash);
        jsonObject.addProperty("item_id", itemId);
        jsonObject.addProperty("price", price);
        jsonObject.addProperty("quantity", quantity);
        jsonObject.addProperty("amount_spent", spent);
        jsonObject.addProperty("collected", collected);
        jsonObject.addProperty("slot", slot);
        // FIX: call name() method on enum for string representation
        jsonObject.addProperty("offer_state", offerState != null ? offerState.name() : "UNKNOWN");
        jsonObject.addProperty("offer_total_quantity", offerTotalQuantity);
        jsonObject.addProperty("copilot_price_used", copilotPriceUsed);
        jsonObject.addProperty("was_copilot_suggestion", wasCopilotSuggestion);
        jsonObject.addProperty("current_cash_stack", currentCashStack);
        jsonObject.addProperty("is_suggestion_skipped", isSuggestionSkipped);
        jsonObject.addProperty("grand_exchange_open", grandExchangeOpen);
        jsonObject.addProperty("is_price_graph_website", isPriceGraphWebsite);
        jsonObject.addProperty("status", status != null ? status.name().toLowerCase() : "unknown");

        return jsonObject;
    }

    public void resetSkipSuggestion() {
        this.isSuggestionSkipped = false;
    }

    public AccountStatus copy() {
        return new AccountStatus(
                this.accountHash, this.itemId, this.price, this.quantity, this.spent,
                this.collected, this.offerState, this.offerTotalQuantity, this.copilotPriceUsed,
                this.wasCopilotSuggestion, this.currentCashStack, this.isSuggestionSkipped,
                this.slot, this.status
        );
    }
}
