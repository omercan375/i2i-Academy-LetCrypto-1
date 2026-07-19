package org.example.i2iacademyletcrypto1.ai_query_logs.prompt;

import org.example.i2iacademyletcrypto1.ai_query_logs.dto.AiContextDto;
import org.example.i2iacademyletcrypto1.trade_transaction.dto.TradeHistoryDto;
import org.example.i2iacademyletcrypto1.user_assets.dto.PortfolioDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class AiPromptBuilder {

    public String build(
            String userQuery,
            AiContextDto context
    ) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                You are CryptoPal AI, a cryptocurrency portfolio and market analysis assistant.

                Rules:
                - Answer only questions about the user's account, wallet, portfolio,
                  cryptocurrency prices, price trends and transaction history.
                - Do not guarantee future profits or certain price movements.
                - Do not claim that any investment will definitely increase or decrease.
                - Clearly state when available data is insufficient.
                - Use the supplied data only.
                - Do not invent account balances, transactions or prices.
                - Respond in the same language as the user's question.
                - Keep the response clear and readable.
                - Markdown formatting may be used.
                - This information is analytical and not financial advice.

                """);

        appendWallet(prompt, context);
        appendPortfolio(prompt, context);
        appendTransactions(prompt, context);
        appendCurrentPrices(prompt, context);

        prompt.append("\nUSER QUESTION:\n")
                .append(sanitize(userQuery))
                .append("\n\n");

        prompt.append("""
                Generate an answer based only on the context above.
                If the question is outside the allowed cryptocurrency and account scope,
                politely state that you can only answer CryptoPal-related questions.
                """);

        return prompt.toString();
    }

    private void appendWallet(
            StringBuilder prompt,
            AiContextDto context
    ) {
        prompt.append("USER WALLET:\n");

        BigDecimal cashBalance = context.getCashBalance();

        if (cashBalance == null) {
            prompt.append("- Cash balance unavailable\n\n");
            return;
        }

        prompt.append("- Cash balance: ")
                .append(cashBalance.toPlainString())
                .append("\n\n");
    }

    private void appendPortfolio(
            StringBuilder prompt,
            AiContextDto context
    ) {
        prompt.append("USER PORTFOLIO:\n");

        if (context.getPortfolio() == null
                || context.getPortfolio().isEmpty()) {
            prompt.append("- User currently holds no crypto assets\n\n");
            return;
        }

        for (PortfolioDto asset : context.getPortfolio()) {
            prompt.append("- Symbol: ")
                    .append(asset.getAssetSymbol())
                    .append(", quantity: ")
                    .append(asset.getAssetQuantity())
                    .append(", current worth: ")
                    .append(asset.getTotalWorth())
                    .append("\n");
        }

        prompt.append("\n");
    }

    private void appendTransactions(
            StringBuilder prompt,
            AiContextDto context
    ) {
        prompt.append("RECENT TRANSACTIONS:\n");

        if (context.getRecentTransactions() == null
                || context.getRecentTransactions().isEmpty()) {
            prompt.append("- No recent transactions\n\n");
            return;
        }

        for (TradeHistoryDto trade : context.getRecentTransactions()) {
            prompt.append("- ")
                    .append(trade.getTradeType())
                    .append(" ")
                    .append(trade.getCryptoAmount())
                    .append(" ")
                    .append(trade.getAssetSymbol())
                    .append(" at execution price ")
                    .append(trade.getExecutionPrice())
                    .append(", total amount ")
                    .append(trade.getTotalAmount())
                    .append("\n");
        }

        prompt.append("\n");
    }

    private void appendCurrentPrices(
            StringBuilder prompt,
            AiContextDto context
    ) {
        prompt.append("CURRENT MARKET PRICES FROM REDIS:\n");

        if (context.getCurrentPrices() == null
                || context.getCurrentPrices().isEmpty()) {
            prompt.append("- Current prices unavailable\n\n");
            return;
        }

        for (Map.Entry<String, BigDecimal> entry
                : context.getCurrentPrices().entrySet()) {

            prompt.append("- ")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue().toPlainString())
                    .append("\n");
        }

        prompt.append("\n");
    }

    private String sanitize(String input) {
        if (input == null) {
            return "";
        }

        return input
                .replace("\u0000", "")
                .trim();
    }
}