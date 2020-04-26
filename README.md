# Example request


* 1. currencies/{currency}
localhost:8080/currencies/BTC?filter=USD,PLN,EUR   
localhost:8080/currencies/BTC?filter=USD   
localhost:8080/currencies/BTC  

w przypadku braku parametry filter zwraca domyslnie 19 pierwszych rekordow z gieldy CoinBene

* 2. currencies/exchange
RequestBody{
	"from":"BTC",
	"to":["EUR","PLN"],
	"amount": 100
}
