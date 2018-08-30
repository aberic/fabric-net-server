package bean

import (
	"encoding/json"
	"fmt"
	"time"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

// 合同全集详情
// 本条记录主键key由成员ID和合同ID联合组成，具备唯一性
type Compact struct {
	Timestamp        int64  `json:"timestamp"`        // 本条记录创建时间戳
	Uid              string `json:"uid"`              // 用户唯一ID（32位MD5值）
	LoanAmount       string `json:"loanAmount"`       // 用户贷款金额
	ApplyDate        string `json:"applyDate"`        // 申请日期
	CompactStartDate string `json:"compactStartDate"` // 贷款开始日期
	CompactEndDate   string `json:"compactEndDate"`   // 贷款计划终止日期
	RealEndDate      string `json:"realEndDate"`      // 贷款实际终止日期
}

// 贷款操作
// args：UID、贷款金额、申请日期、贷款开始日期、贷款计划终止日期、合同ID
// name：成员名称
func Loan(stub shim.ChaincodeStubInterface, args []string, name string) error {
	if len(args) != 6 {
		return fmt.Errorf("Parameter count error while Loan, count must 5")
	}
	if len(args[0]) != 32 {
		return fmt.Errorf("Parameter uid length error while Loan, 32 is right")
	}
	if len(args[2]) != 14 {
		return fmt.Errorf("Parameter ApplyDate length error while Loan, 14 is right")
	}
	if len(args[3]) != 14 {
		return fmt.Errorf("Parameter CompactStartDate length error while Loan, 14 is right")
	}
	if len(args[4]) != 24 {
		return fmt.Errorf("Parameter CompactEndDate length error while Loan, 14 is right")
	}
	var compact Compact
	compact.Uid = args[0]
	compact.LoanAmount = args[1]
	compact.ApplyDate = args[2]
	compact.CompactStartDate = args[3]
	compact.CompactEndDate = args[4]
	compact.Timestamp = time.Now().Unix()

	compactJsonBytes, err := json.Marshal(&compact) // Json序列化
	if err != nil {
		return fmt.Errorf("Json serialize Compact fail while Loan, compact id = " + args[5])
	}
	// 生成合同联合主键
	key, err := stub.CreateCompositeKey("Compact", []string{name, args[5]})
	if err != nil {
		return fmt.Errorf("Failed to CreateCompositeKey while Loan")
	}
	// 保存合同信息
	err = stub.PutState(key, compactJsonBytes)
	if err != nil {
		return fmt.Errorf("Failed to PutState while Loan, compact id = " + args[5])
	}
	return nil
}
