package com.arnasmat.dcrowd.data.sol;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/LFDT-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.7.0.
 */
@SuppressWarnings("rawtypes")
public class CrowdSourcing extends Contract {
    public static final String BINARY = "0x608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550611fab806100606000396000f3fe6080604052600436106100705760003560e01c80631a86e67d1161004e5780631a86e67d1461010c5780634e94e824146101355780636c563abe1461015e5780636db7a9601461017a57610070565b806308e38af9146100755780630e4877d61461009e578063107046bd146100c9575b600080fd5b34801561008157600080fd5b5061009c60048036038101906100979190611258565b6101a3565b005b3480156100aa57600080fd5b506100b36103b3565b6040516100c091906112c6565b60405180910390f35b3480156100d557600080fd5b506100f060048036038101906100eb9190611258565b6103d7565b60405161010397969594939291906113a4565b60405180910390f35b34801561011857600080fd5b50610133600480360381019061012e919061167a565b6105ee565b005b34801561014157600080fd5b5061015c60048036038101906101579190611258565b6108a7565b005b61017860048036038101906101739190611258565b6109f4565b005b34801561018657600080fd5b506101a1600480360381019061019c9190611258565b610d4b565b005b6000600182815481106101b9576101b8611751565b5b906000526020600020906008020190506000816006018260050154815481106101e5576101e4611751565b5b906000526020600020906006020190508160070160009054906101000a900460ff16610246576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161023d906117cc565b60405180910390fd5b8060050160009054906101000a900460ff1615610298576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161028f90611838565b60405180910390fd5b80600001600101544210156102e2576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016102d9906118a4565b60405180910390fd5b80600001600001548260040154106103a45760018160050160006101000a81548160ff0219169083151502179055506103438260000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168260020154610e60565b6001826006018054905061035791906118f3565b826005015410156103815781600501600081548092919061037790611927565b919050555061039f565b60008260070160006101000a81548160ff0219169083151502179055505b6103ae565b6103ad83610f11565b5b505050565b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600181815481106103e757600080fd5b90600052602060002090600802016000915090508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060010180546104309061199e565b80601f016020809104026020016040519081016040528092919081815260200182805461045c9061199e565b80156104a95780601f1061047e576101008083540402835291602001916104a9565b820191906000526020600020905b81548152906001019060200180831161048c57829003601f168201915b5050505050908060020180546104be9061199e565b80601f01602080910402602001604051908101604052809291908181526020018280546104ea9061199e565b80156105375780601f1061050c57610100808354040283529160200191610537565b820191906000526020600020905b81548152906001019060200180831161051a57829003601f168201915b50505050509080600301805461054c9061199e565b80601f01602080910402602001604051908101604052809291908181526020018280546105789061199e565b80156105c55780601f1061059a576101008083540402835291602001916105c5565b820191906000526020600020905b8154815290600101906020018083116105a857829003601f168201915b5050505050908060040154908060050154908060070160009054906101000a900460ff16905087565b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff160361067c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161067390611a41565b60405180910390fd5b60008151116106c0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106b790611aad565b60405180910390fd5b6000600180816001815401808255809150500390600052602060002090600802019050338160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508481600101908051906020019061073e92919061116b565b508381600201908051906020019061075792919061116b565b508281600301908051906020019061077092919061116b565b50600081600401819055506000816005018190555060018160070160006101000a81548160ff02191690831515021790555060005b82518110156108635760008260060160018160018154018082558091505003906000526020600020906006020190508382815181106107e7576107e6611751565b5b602002602001015160000151816000016000018190555083828151811061081157610810611751565b5b60200260200101516020015181600001600101819055506000816002018190555060008160050160006101000a81548160ff02191690831515021790555050808061085b90611927565b9150506107a5565b507f1ac2092e16e28eef29d1832f3dc69da1b21284a1f3159d3b26cb238b22fe4f8a6001805490506040516108989190611acd565b60405180910390a15050505050565b6000600182815481106108bd576108bc611751565b5b906000526020600020906008020190508060070160009054906101000a900460ff1661091e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610915906117cc565b60405180910390fd5b60008160060182600501548154811061093a57610939611751565b5b906000526020600020906006020190508060050160009054906101000a900460ff161561099c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161099390611838565b60405180910390fd5b7f3f9ee693372748925082c0c5ba09ecffc5fddfeecf993becff877eac8a1e08bc81600001600101548260000160000154846004015484600201546040516109e79493929190611ae8565b60405180910390a1505050565b6001805490508110610a3b576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610a3290611b79565b60405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff1631341115610a95576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610a8c90611c0b565b60405180910390fd5b60003411610ad8576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610acf90611c77565b60405180910390fd5b600060018281548110610aee57610aed611751565b5b906000526020600020906008020190508060070160009054906101000a900460ff16610b4f576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b4690611ce3565b60405180910390fd5b600081600601826005015481548110610b6b57610b6a611751565b5b9060005260206000209060060201905042816000016001015411610bc4576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610bbb90611d4f565b60405180910390fd5b348160040160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254610c159190611d6f565b9250508190555034816002016000828254610c309190611d6f565b9250508190555034826004016000828254610c4b9190611d6f565b9250508190555060008160040160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205403610d015780600301339080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b7f17e2effa169c5dde193942abd21f6d49e7d32c15ab843da440cb2abdd10f1911833484600401548460020154604051610d3e9493929190611ae8565b60405180910390a1505050565b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161480610e15575060018181548110610db457610db3611751565b5b906000526020600020906008020160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b610e54576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610e4b90611e37565b60405180910390fd5b610e5d81610f11565b50565b60008273ffffffffffffffffffffffffffffffffffffffff1682604051610e8690611e88565b60006040518083038185875af1925050503d8060008114610ec3576040519150601f19603f3d011682016040523d82523d6000602084013e610ec8565b606091505b5050905080610f0c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610f0390611ee9565b60405180910390fd5b505050565b600060018281548110610f2757610f26611751565b5b9060005260206000209060080201905060008160070160006101000a81548160ff021916908315150217905550610f62828260050154610f66565b5050565b600060018381548110610f7c57610f7b611751565b5b906000526020600020906008020190506000816006018381548110610fa457610fa3611751565b5b9060005260206000209060060201905060005b8160030180549050811015611164576000826003018281548110610fde57610fdd611751565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905060008360040160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050600081111561114f5760008460040160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555060008273ffffffffffffffffffffffffffffffffffffffff16826040516110c790611e88565b60006040518083038185875af1925050503d8060008114611104576040519150601f19603f3d011682016040523d82523d6000602084013e611109565b606091505b505090508061114d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161114490611f55565b60405180910390fd5b505b5050808061115c90611927565b915050610fb7565b5050505050565b8280546111779061199e565b90600052602060002090601f01602090048101928261119957600085556111e0565b82601f106111b257805160ff19168380011785556111e0565b828001600101855582156111e0579182015b828111156111df5782518255916020019190600101906111c4565b5b5090506111ed91906111f1565b5090565b5b8082111561120a5760008160009055506001016111f2565b5090565b6000604051905090565b600080fd5b600080fd5b6000819050919050565b61123581611222565b811461124057600080fd5b50565b6000813590506112528161122c565b92915050565b60006020828403121561126e5761126d611218565b5b600061127c84828501611243565b91505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006112b082611285565b9050919050565b6112c0816112a5565b82525050565b60006020820190506112db60008301846112b7565b92915050565b600081519050919050565b600082825260208201905092915050565b60005b8381101561131b578082015181840152602081019050611300565b8381111561132a576000848401525b50505050565b6000601f19601f8301169050919050565b600061134c826112e1565b61135681856112ec565b93506113668185602086016112fd565b61136f81611330565b840191505092915050565b61138381611222565b82525050565b60008115159050919050565b61139e81611389565b82525050565b600060e0820190506113b9600083018a6112b7565b81810360208301526113cb8189611341565b905081810360408301526113df8188611341565b905081810360608301526113f38187611341565b9050611402608083018661137a565b61140f60a083018561137a565b61141c60c0830184611395565b98975050505050505050565b600080fd5b600080fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b61146a82611330565b810181811067ffffffffffffffff8211171561148957611488611432565b5b80604052505050565b600061149c61120e565b90506114a88282611461565b919050565b600067ffffffffffffffff8211156114c8576114c7611432565b5b6114d182611330565b9050602081019050919050565b82818337600083830152505050565b60006115006114fb846114ad565b611492565b90508281526020810184848401111561151c5761151b61142d565b5b6115278482856114de565b509392505050565b600082601f83011261154457611543611428565b5b81356115548482602086016114ed565b91505092915050565b600067ffffffffffffffff82111561157857611577611432565b5b602082029050602081019050919050565b600080fd5b600080fd5b6000604082840312156115a9576115a861158e565b5b6115b36040611492565b905060006115c384828501611243565b60008301525060206115d784828501611243565b60208301525092915050565b60006115f66115f18461155d565b611492565b9050808382526020820190506040840283018581111561161957611618611589565b5b835b81811015611642578061162e8882611593565b84526020840193505060408101905061161b565b5050509392505050565b600082601f83011261166157611660611428565b5b81356116718482602086016115e3565b91505092915050565b6000806000806080858703121561169457611693611218565b5b600085013567ffffffffffffffff8111156116b2576116b161121d565b5b6116be8782880161152f565b945050602085013567ffffffffffffffff8111156116df576116de61121d565b5b6116eb8782880161152f565b935050604085013567ffffffffffffffff81111561170c5761170b61121d565b5b6117188782880161152f565b925050606085013567ffffffffffffffff8111156117395761173861121d565b5b6117458782880161164c565b91505092959194509250565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b7f50726f6a656374206d7573742062652061637469766500000000000000000000600082015250565b60006117b66016836112ec565b91506117c182611780565b602082019050919050565b600060208201905081810360008301526117e5816117a9565b9050919050565b7f4d696c6573746f6e652073686f756c64206265206e6f74207265616368656400600082015250565b6000611822601f836112ec565b915061182d826117ec565b602082019050919050565b6000602082019050818103600083015261185181611815565b9050919050565b7f43616e206f6e6c79206368656b636b206166746572206d696c6573746f6e6500600082015250565b600061188e601f836112ec565b915061189982611858565b602082019050919050565b600060208201905081810360008301526118bd81611881565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b60006118fe82611222565b915061190983611222565b92508282101561191c5761191b6118c4565b5b828203905092915050565b600061193282611222565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203611964576119636118c4565b5b600182019050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b600060028204905060018216806119b657607f821691505b6020821081036119c9576119c861196f565b5b50919050565b7f53797374656d206f776e65722063616e2774206372656174652070726f6a656360008201527f7473000000000000000000000000000000000000000000000000000000000000602082015250565b6000611a2b6022836112ec565b9150611a36826119cf565b604082019050919050565b60006020820190508181036000830152611a5a81611a1e565b9050919050565b7f4d7573742068617665206174206c65617374206f6e65206d696c6573746f6e65600082015250565b6000611a976020836112ec565b9150611aa282611a61565b602082019050919050565b60006020820190508181036000830152611ac681611a8a565b9050919050565b6000602082019050611ae2600083018461137a565b92915050565b6000608082019050611afd600083018761137a565b611b0a602083018661137a565b611b17604083018561137a565b611b24606083018461137a565b95945050505050565b7f50726f6a65637420646f65736e27742065786973740000000000000000000000600082015250565b6000611b636015836112ec565b9150611b6e82611b2d565b602082019050919050565b60006020820190508181036000830152611b9281611b56565b9050919050565b7f596f7520646f6e74206861766520656e6f756768206d6f6e657920746f20667560008201527f6e64000000000000000000000000000000000000000000000000000000000000602082015250565b6000611bf56022836112ec565b9150611c0082611b99565b604082019050919050565b60006020820190508181036000830152611c2481611be8565b9050919050565b7f46756e6420616d6f756e742063616e2774206265203000000000000000000000600082015250565b6000611c616016836112ec565b9150611c6c82611c2b565b602082019050919050565b60006020820190508181036000830152611c9081611c54565b9050919050565b7f50726f6a656374206d7573742062652061637469766520746f2066756e640000600082015250565b6000611ccd601e836112ec565b9150611cd882611c97565b602082019050919050565b60006020820190508181036000830152611cfc81611cc0565b9050919050565b7f596f752063616e27742066756e6420616674657220646561646c696e65000000600082015250565b6000611d39601d836112ec565b9150611d4482611d03565b602082019050919050565b60006020820190508181036000830152611d6881611d2c565b9050919050565b6000611d7a82611222565b9150611d8583611222565b9250827fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff03821115611dba57611db96118c4565b5b828201905092915050565b7f4f6e6c792073797374656d206f776e6572206f722070726f6a656374206f776e60008201527f65722063616e2073746f702070726f6a65637473000000000000000000000000602082015250565b6000611e216034836112ec565b9150611e2c82611dc5565b604082019050919050565b60006020820190508181036000830152611e5081611e14565b9050919050565b600081905092915050565b50565b6000611e72600083611e57565b9150611e7d82611e62565b600082019050919050565b6000611e9382611e65565b9150819050919050565b7f5472616e7366657220746f2063726561746f72206661696c6564000000000000600082015250565b6000611ed3601a836112ec565b9150611ede82611e9d565b602082019050919050565b60006020820190508181036000830152611f0281611ec6565b9050919050565b7f526566756e64206661696c656400000000000000000000000000000000000000600082015250565b6000611f3f600d836112ec565b9150611f4a82611f09565b602082019050919050565b60006020820190508181036000830152611f6e81611f32565b905091905056fea2646970667358221220b6f55e1a2ca4d9a6632c394a628a1cc9e54b866e26d9008691bed8097395ac3264736f6c634300080d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_PROJECTS = "projects";

    public static final String FUNC_SYSOWNER = "sysOwner";

    public static final String FUNC_CREATEPROJECT = "createProject";

    public static final String FUNC_FUNDPROJECT = "fundProject";

    public static final String FUNC_CHECKCURRENTMILESTONE = "checkCurrentMilestone";

    public static final String FUNC_CHECKMILESTONE = "checkMilestone";

    public static final String FUNC_STOPPROJECT = "stopProject";

    public static final Event CURRENTMILESTONESTATUS_EVENT = new Event("CurrentMilestoneStatus", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PROJECTCREATED_EVENT = new Event("ProjectCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event PROJECTFUNDED_EVENT = new Event("ProjectFunded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
        _addresses.put("1764434538176", "0x9aB8Cc7783141F25ead533F990b51d76116a7d3A");
        _addresses.put("1764505264260", "0x1EA51DE187E261B4B9E2Db96cA63C333192D43e6");
    }

    @Deprecated
    protected CrowdSourcing(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CrowdSourcing(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected CrowdSourcing(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected CrowdSourcing(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<CurrentMilestoneStatusEventResponse> getCurrentMilestoneStatusEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CURRENTMILESTONESTATUS_EVENT, transactionReceipt);
        ArrayList<CurrentMilestoneStatusEventResponse> responses = new ArrayList<CurrentMilestoneStatusEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CurrentMilestoneStatusEventResponse typedResponse = new CurrentMilestoneStatusEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.deadline = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.goalAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.totalFunded = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.currentMileStoneTotalFunded = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CurrentMilestoneStatusEventResponse getCurrentMilestoneStatusEventFromLog(
            Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CURRENTMILESTONESTATUS_EVENT, log);
        CurrentMilestoneStatusEventResponse typedResponse = new CurrentMilestoneStatusEventResponse();
        typedResponse.log = log;
        typedResponse.deadline = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.goalAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.totalFunded = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.currentMileStoneTotalFunded = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        return typedResponse;
    }

    public Flowable<CurrentMilestoneStatusEventResponse> currentMilestoneStatusEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCurrentMilestoneStatusEventFromLog(log));
    }

    public Flowable<CurrentMilestoneStatusEventResponse> currentMilestoneStatusEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CURRENTMILESTONESTATUS_EVENT));
        return currentMilestoneStatusEventFlowable(filter);
    }

    public static List<ProjectCreatedEventResponse> getProjectCreatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PROJECTCREATED_EVENT, transactionReceipt);
        ArrayList<ProjectCreatedEventResponse> responses = new ArrayList<ProjectCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ProjectCreatedEventResponse typedResponse = new ProjectCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.projectIdx = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ProjectCreatedEventResponse getProjectCreatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PROJECTCREATED_EVENT, log);
        ProjectCreatedEventResponse typedResponse = new ProjectCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.projectIdx = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ProjectCreatedEventResponse> projectCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getProjectCreatedEventFromLog(log));
    }

    public Flowable<ProjectCreatedEventResponse> projectCreatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PROJECTCREATED_EVENT));
        return projectCreatedEventFlowable(filter);
    }

    public static List<ProjectFundedEventResponse> getProjectFundedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PROJECTFUNDED_EVENT, transactionReceipt);
        ArrayList<ProjectFundedEventResponse> responses = new ArrayList<ProjectFundedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ProjectFundedEventResponse typedResponse = new ProjectFundedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.projectIdx = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.totalFunded = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.currentMilestoneTotalFunded = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ProjectFundedEventResponse getProjectFundedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PROJECTFUNDED_EVENT, log);
        ProjectFundedEventResponse typedResponse = new ProjectFundedEventResponse();
        typedResponse.log = log;
        typedResponse.projectIdx = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.totalFunded = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.currentMilestoneTotalFunded = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        return typedResponse;
    }

    public Flowable<ProjectFundedEventResponse> projectFundedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getProjectFundedEventFromLog(log));
    }

    public Flowable<ProjectFundedEventResponse> projectFundedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PROJECTFUNDED_EVENT));
        return projectFundedEventFlowable(filter);
    }

    public RemoteFunctionCall<Tuple7<String, String, String, String, BigInteger, BigInteger, Boolean>> projects(
            BigInteger param0) {
        final Function function = new Function(FUNC_PROJECTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple7<String, String, String, String, BigInteger, BigInteger, Boolean>>(function,
                new Callable<Tuple7<String, String, String, String, BigInteger, BigInteger, Boolean>>() {
                    @Override
                    public Tuple7<String, String, String, String, BigInteger, BigInteger, Boolean> call(
                            ) throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<String, String, String, String, BigInteger, BigInteger, Boolean>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (Boolean) results.get(6).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> sysOwner() {
        final Function function = new Function(FUNC_SYSOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> createProject(String _name,
            String _headerImageUrl, String _description, List<MilestoneInfo> _milestones) {
        final Function function = new Function(
                FUNC_CREATEPROJECT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_headerImageUrl), 
                new org.web3j.abi.datatypes.Utf8String(_description), 
                new org.web3j.abi.datatypes.DynamicArray<MilestoneInfo>(MilestoneInfo.class, _milestones)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> fundProject(BigInteger _projectIdx,
            BigInteger weiValue) {
        final Function function = new Function(
                FUNC_FUNDPROJECT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_projectIdx)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> checkCurrentMilestone(BigInteger _projectIdx) {
        final Function function = new Function(
                FUNC_CHECKCURRENTMILESTONE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_projectIdx)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> checkMilestone(BigInteger _projectIdx) {
        final Function function = new Function(
                FUNC_CHECKMILESTONE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_projectIdx)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> stopProject(BigInteger _projectIdx) {
        final Function function = new Function(
                FUNC_STOPPROJECT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_projectIdx)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static CrowdSourcing load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new CrowdSourcing(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static CrowdSourcing load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CrowdSourcing(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static CrowdSourcing load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new CrowdSourcing(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static CrowdSourcing load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CrowdSourcing(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CrowdSourcing> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CrowdSourcing.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<CrowdSourcing> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CrowdSourcing.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<CrowdSourcing> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CrowdSourcing.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<CrowdSourcing> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CrowdSourcing.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class MilestoneInfo extends StaticStruct {
        public BigInteger goalAmount;

        public BigInteger deadline;

        public MilestoneInfo(BigInteger goalAmount, BigInteger deadline) {
            super(new org.web3j.abi.datatypes.generated.Uint256(goalAmount), 
                    new org.web3j.abi.datatypes.generated.Uint256(deadline));
            this.goalAmount = goalAmount;
            this.deadline = deadline;
        }

        public MilestoneInfo(Uint256 goalAmount, Uint256 deadline) {
            super(goalAmount, deadline);
            this.goalAmount = goalAmount.getValue();
            this.deadline = deadline.getValue();
        }
    }

    public static class CurrentMilestoneStatusEventResponse extends BaseEventResponse {
        public BigInteger deadline;

        public BigInteger goalAmount;

        public BigInteger totalFunded;

        public BigInteger currentMileStoneTotalFunded;
    }

    public static class ProjectCreatedEventResponse extends BaseEventResponse {
        public BigInteger projectIdx;
    }

    public static class ProjectFundedEventResponse extends BaseEventResponse {
        public BigInteger projectIdx;

        public BigInteger amount;

        public BigInteger totalFunded;

        public BigInteger currentMilestoneTotalFunded;
    }
}
