import java.util.*;


class Stock {
    String symbol;
    double price;

    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    
    void updatePrice() {
        double changePercent = (Math.random() * 4) - 2; // -2% to +2%
        price += price * (changePercent / 100);
        price = Math.round(price * 100.0) / 100.0; // round to 2 decimal places
    }
}


class Trade {
    Stock stock;
    int quantity;
    double buyPrice;

    Trade(Stock stock, int quantity, double buyPrice) {
        this.stock = stock;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
    }

    double currentValue() {
        return quantity * stock.price;
    }

    double profitLoss() {
        return (stock.price - buyPrice) * quantity;
    }
}


class Portfolio {
    ArrayList<Trade> trades = new ArrayList<>();
    double totalProfit = 0;

    // Buy stock and add to portfolio
    void buyStock(Stock stock, int qty) {
        trades.add(new Trade(stock, qty, stock.price));
        System.out.println("✅ Bought " + qty + " shares of " + stock.symbol + " at $" + stock.price);
    }

    
    void sellStock(String symbol, int qty) {
        for (int i = 0; i < trades.size(); i++) {
            Trade t = trades.get(i);
            if (t.stock.symbol.equals(symbol)) {
                if (qty <= t.quantity) {
                    double sellValue = qty * t.stock.price;
                    double buyValue = qty * t.buyPrice;
                    double pl = sellValue - buyValue;
                    totalProfit += pl;
                    t.quantity -= qty;
                    if (t.quantity == 0) {
                        trades.remove(i);
                    }
                    System.out.println("✅ Sold " + qty + " shares of " + symbol + " at $" + t.stock.price);
                    System.out.println("💹 Profit/Loss from this transaction: $" + pl);
                    return;
                } else {
                    System.out.println("⚠️ Not enough shares to sell.");
                    return;
                }
            }
        }
        System.out.println("❌ You don't own this stock.");
    }

    // Show full portfolio status
    void displayPortfolio() {
        double totalValue = 0, totalPL = 0;
        System.out.println("\n📊 ===== Portfolio Summary =====");
        if (trades.isEmpty()) {
            System.out.println("You have no active holdings.");
        }
        for (Trade t : trades) {
            double value = t.currentValue();
            double pl = t.profitLoss();
            System.out.println("📦 Stock: " + t.stock.symbol + " | Qty: " + t.quantity +
                               " | Buy @ $" + t.buyPrice + " | Now @ $" + t.stock.price +
                               " | Value: $" + value + " | P/L: $" + pl);
            totalValue += value;
            totalPL += pl;
        }
        System.out.println("💼 Total Portfolio Value: $" + totalValue);
        System.out.println("📈 Unrealized Profit/Loss: $" + totalPL);
        System.out.println("💰 Realized Profit/Loss: $" + totalProfit);
        System.out.println("============================\n");
    }

    // Show buy details only
    void viewBuyDetails() {
        System.out.println("\n📘 Buy Details:");
        if (trades.isEmpty()) {
            System.out.println("You haven't bought any stocks yet.");
            return;
        }
        for (Trade t : trades) {
            System.out.printf("🔹 %s: %d shares bought at $%.2f each\n", t.stock.symbol, t.quantity, t.buyPrice);
        }
    }
}


public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Portfolio myPortfolio = new Portfolio();

        // Create some dummy stocks (initial prices)
        Stock apple = new Stock("AAPL", 150);
        Stock google = new Stock("GOOGL", 2800);
        Stock tesla = new Stock("TSLA", 700);

        // Add to market list
        HashMap<String, Stock> market = new HashMap<>();
        market.put("AAPL", apple);
        market.put("GOOGL", google);
        market.put("TSLA", tesla);

        System.out.println("📈 Welcome to the Stock Trading Platform!\n");

        while (true) {
            System.out.println("==== MENU ====");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Remaining Stocks");
            System.out.println("6. View Buy Details");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int ch = scanner.nextInt();

            if (ch == 1) {
                // Update stock prices
                for (Stock s : market.values()) {
                    s.updatePrice();
                }

                System.out.println("\n📃 Updated Market Prices:");
                for (Stock s : market.values()) {
                    System.out.println(s.symbol + " - $" + s.price + recommend(s));
                }
                System.out.println();
            } else if (ch == 2) {
                System.out.print("Enter Stock Symbol to Buy: ");
                String symbol = scanner.next().toUpperCase();
                if (market.containsKey(symbol)) {
                    System.out.print("Enter quantity: ");
                    int qty = scanner.nextInt();
                    myPortfolio.buyStock(market.get(symbol), qty);
                } else {
                    System.out.println("⚠️ Invalid stock symbol!\n");
                }
            } else if (ch == 3) {
                System.out.print("Enter Stock Symbol to Sell: ");
                String symbol = scanner.next().toUpperCase();
                System.out.print("Enter quantity to sell: ");
                int qty = scanner.nextInt();
                myPortfolio.sellStock(symbol, qty);
            } else if (ch == 4) {
                myPortfolio.displayPortfolio();
            } else if (ch == 5) {
                System.out.println("Stocks left in market:");
                for (Stock s : market.values()) {
                    System.out.println("🔸 " + s.symbol + " - $" + s.price);
                }
                System.out.println();
            } else if (ch == 6) {
                myPortfolio.viewBuyDetails();
            } else if (ch == 7) {
                System.out.println("👋 Thanks for using the platform. Goodbye!");
                break;
            } else {
                System.out.println("❌ Invalid option. Try again.\n");
            }
        }

        scanner.close();
    }

    
    static String recommend(Stock s) {
        if (s.price < 100) return " (🟢 Good to BUY)";
        if (s.price > 2000) return " (🔴 Consider SELLING)";
        return " (🟡 HOLD)";
    }
}
