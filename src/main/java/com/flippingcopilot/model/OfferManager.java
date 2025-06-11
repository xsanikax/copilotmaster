package com.flippingcopilot.model;

import java.time.Instant;
import java.util.Collection; // Import Collection
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GrandExchangeOfferState; // Import GrandExchangeOfferState

import javax.inject.Inject; // Import Inject

@Slf4j
@Singleton
public class OfferManager {

    private final Map<Long, Map<Integer, SavedOffer>> accountHashToOffers = new HashMap<>();

    // Not a constructor dependency, but part of client's internal state
    private int lastViewedSlotItemId = -1;
    private int lastViewedSlotItemPrice = -1;
    private int lastViewedSlotPriceTime = 0; // seconds epoch

    // Inject required dependencies if any
    @Inject
    public OfferManager() {
        // Constructor, can be empty if no specific init logic needed here.
    }

    public void addOffer(int slot, GrandExchangeOffer offer) {
        SavedOffer savedOffer = SavedOffer.fromGrandExchangeOffer(offer);
        // Assuming client.getAccountHash() is available and provides the current account hash
        Long accountHash = null; // Replace with actual client.getAccountHash() from GrandExchangeOfferEventHandler
        if (accountHash == null) {
            return; // Cannot save without account hash
        }
        accountHashToOffers.computeIfAbsent(accountHash, k -> new HashMap<>()).put(slot, savedOffer);
    }

    public SavedOffer getOffer(int slot) {
        // Assuming client.getAccountHash() is available
        Long accountHash = null; // Replace with actual client.getAccountHash()
        if (accountHash == null) {
            return null;
        }
        return accountHashToOffers.getOrDefault(accountHash, Collections.emptyMap()).get(slot);
    }

    public void ackOffer(int slot) {
        // Assuming client.getAccountHash() is available
        Long accountHash = null; // Replace with actual client.getAccountHash()
        if (accountHash == null) {
            return;
        }
        SavedOffer offer = accountHashToOffers.getOrDefault(accountHash, Collections.emptyMap()).get(slot);
        if (offer != null) {
            offer.setAcked(true);
        }
    }

    public void saveAll() {
        // This method would typically persist all offers to storage (e.g., JSON file, database)
        log.debug("Saving all offers (not implemented for simplicity)");
    }

    // --- NEW: Getter for offers collection ---
    public Collection<SavedOffer> getOffers() {
        // This will return all offers for the current account.
        // You might need to adjust based on how you want to filter/scope offers.
        // Assuming current account hash is obtained from client or passed in.
        Long currentAccountHash = null; // Replace with actual client.getAccountHash() if needed here
        if (currentAccountHash != null) {
            return accountHashToOffers.getOrDefault(currentAccountHash, Collections.emptyMap()).values();
        }
        // If no account hash context, return all offers from all accounts (less likely needed)
        Collection<SavedOffer> allOffers = new ArrayList<>();
        accountHashToOffers.values().forEach(map -> allOffers.addAll(map.values()));
        return allOffers;
    }

    public int getLastViewedSlotItemId() {
        return lastViewedSlotItemId;
    }

    public void setLastViewedSlotItemId(int lastViewedSlotItemId) {
        this.lastViewedSlotItemId = lastViewedSlotItemId;
    }

    public int getLastViewedSlotItemPrice() {
        return lastViewedSlotItemPrice;
    }

    public void setLastViewedSlotItemPrice(int lastViewedSlotItemPrice) {
        this.lastViewedSlotItemPrice = lastViewedSlotItemPrice;
    }

    public int getLastViewedSlotPriceTime() {
        return lastViewedSlotPriceTime;
    }

    public void setLastViewedSlotPriceTime(int lastViewedSlotPriceTime) {
        this.lastViewedSlotPriceTime = lastViewedSlotPriceTime;
    }

    public void setViewedSlotItemId(int currentItemId) {
        // This method likely exists in your OfferManager or a related class.
        // Assuming it sets the ID of the item currently being viewed in a GE slot.
    }

    public void setViewedSlotItemPrice(int price) {
        // This method likely exists.
    }
}
