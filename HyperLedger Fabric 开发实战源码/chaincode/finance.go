package main

import (
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"

	"github.com/hyperledger/fabric/aberic/chaincode/go/finance/bean"
	"github.com/hyperledger/fabric/aberic/chaincode/go/finance/utils"
)

type Finance struct {
}

func (t *Finance) Init(stub shim.ChaincodeStubInterface) peer.Response {
	args := stub.GetStringArgs()
	if len(args) != 0 {
		return shim.Error("Parameter error while Init")
	}
	return shim.Success(nil)
}

func (t *Finance) Invoke(stub shim.ChaincodeStubInterface) peer.Response {
	fn, args := stub.GetFunctionAndParameters()
	switch fn {
	case "loan": // 记录贷款数据
		return loan(stub, args)
	default:
		return shim.Error("Unknown func type while Invoke, please check")
	}
}

// 记录贷款数据
func loan(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	name, err := utils.GetCreatorName(stub)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = bean.Loan(stub, args, name)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success([]byte("记录贷款数据成功"))
}

func main() {
	if err := shim.Start(new(Finance)); err != nil {
		fmt.Printf("Chaincode startup error: %s", err)
	}
}
