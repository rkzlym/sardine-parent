package sardine.aes.domain.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sardine.aes.domain.enums.ExchangeEnum;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OkexOptions implements Options {

  @Builder.Default
  private String restHost = "https://www.okex.com/";

  @Builder.Default
  private String websocketHost = "wss://awspush.okex.com:8443/ws/v3";

  private String apiKey;

  private String secretKey;

  @Builder.Default
  private boolean websocketAutoConnect = true;

  @Override
  public String getApiKey() {
    return this.apiKey;
  }

  @Override
  public String getSecretKey() {
    return this.secretKey;
  }

  @Override
  public ExchangeEnum getExchange() {
    return ExchangeEnum.HUOBI;
  }

  @Override
  public String getRestHost() {
    return this.restHost;
  }

  @Override
  public String getWebSocketHost() {
    return this.websocketHost;
  }

  @Override
  public boolean isWebSocketAutoConnect() {
    return this.websocketAutoConnect;
  }

}